package com.gus.annotation.processor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.gus.annotation.ColumnMapping;
import com.gus.annotation.TableMapping;
import com.gus.enumeration.ImmutableGlobalData;

/**
 * 
 * @author guybe
 * @see META-INF/services/javax.annotation.processing.Processor
 */
public class ImmutableGlobalDataAnnotationProcessor extends AbstractProcessor {
	
	private Types typeUtils;
	private Elements elementUtils;
	private Filer filer;
	private Messager messager;
	
	public ImmutableGlobalDataAnnotationProcessor() {
		// must have a no-args constructor
	}

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
	    super.init(processingEnv);
	    typeUtils = processingEnv.getTypeUtils();
	    elementUtils = processingEnv.getElementUtils();
	    filer = processingEnv.getFiler();
	    messager = processingEnv.getMessager();
	}
	
	/**
	 * <li>Processing may occur before classes have been compiled so we can't use things like 
	 * <code>instanceof</code> or use reflection, we must rely upon the javax language model 
	 * and the (source code) 'elements' passed to us.
	 * <li>AP runs in its own JVM and in order to print error message that persist for the 
	 * end user to see, we must use the <code>messager</code> and not throw exceptions that 
	 * will terminate the jvm. 
	 * <li>The processor is instantiated once but this process method may be called multiple 
	 * times for each 'round' of annotation processing (recursively) on any generated source files.
	 */
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		// Iterate over all @InternalGlobalData annotated elements
	    for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(TableMapping.class)) {
	    	warn(annotatedElement,"INFO:ImmutableGlobalDataAnnotationProcessor processing element kind=%s name=%s with @TableMapping",
	    			annotatedElement.getKind(),
	    			annotatedElement.getSimpleName());
	    	// If this element represents an Enum then were good to go
	  		if(annotatedElement.getKind() == ElementKind.ENUM) {
	  			// Classes and enums are TypeElements
	  			TypeElement enumElement = (TypeElement)annotatedElement;
	  			// Ensure its public
	  			if (!enumElement.getModifiers().contains(Modifier.PUBLIC)) {
	  				error(enumElement, "The enum %s is not public.",
	  		    		enumElement.getQualifiedName().toString());
	  		      	return false;
	  		    }
	  			// Ensure it implements ImmutableGlobalData
	  			boolean ifaceNotFound = true;
	  			List<? extends TypeMirror> interfaces = enumElement.getInterfaces();
	  			for (TypeMirror iface : interfaces) {
	  				if(iface.toString().startsWith(ImmutableGlobalData.class.getCanonicalName())) {
	  					ifaceNotFound = false;
	  				}	
				}
	  			if(ifaceNotFound) {
	  				error(enumElement, "Enum must implement %s?",
	  					ImmutableGlobalData.class.getCanonicalName());
	  				return false;
	  			}
	  			
	  			// Ensure there is a table name mapping
	  			TableMapping tableMapping = enumElement.getAnnotation(TableMapping.class);
	  		    String tablename = tableMapping.name();
	  		    if(Strings.isNullOrEmpty(tablename)) {
	  		    	error(enumElement, "Enum annotated with @%s is missing table name?",
		  					TableMapping.class.getSimpleName());
		  		    return false; // Exit processing?
	  		    }
	  		    for (Element enclosedElement : enumElement.getEnclosedElements()) {
	  		    	if(enclosedElement.getKind() == ElementKind.FIELD) {
	  		    		VariableElement fieldElement = (VariableElement)enclosedElement;
	  		    		ColumnMapping columnMapping = fieldElement.getAnnotation(ColumnMapping.class);
	  		    		if(columnMapping == null) {
	  		  		    	error(enumElement, "Field \"%s\" is missing @%s annotation?",
	  		  		    		fieldElement.getSimpleName(),
	  		  		    		ColumnMapping.class.getSimpleName());
	  		  		    }
	  		    		String columnname = columnMapping.name();
	  		    		if(Strings.isNullOrEmpty(columnname)) {
	  		    			error(enumElement, "Field \"%s\" annotated with @%s is missing column name?",
		  		  		    		fieldElement.getSimpleName(),
		  		  		    		ColumnMapping.class.getSimpleName());
	  		  		    }
	  		    	}
				}

	  		} else {
	  			// User annotated something else?
	  			error(annotatedElement, "Only enums can be annotated with @%s",
	  					TableMapping.class.getSimpleName());
	  		    return true; // Exit processing
	  		}
	    }
		return false;
	}
	
	List<Class<GlobalImmutableData>> getGlobalImmutableDataClasses(String packageName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assertNotNull("No class loader?", classLoader);
		
		URL root = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));
		assertNotNull("No root URL for package name "+packageName+"?", root);
		// Filter .class files.
		File[] files = new File(root.getFile()).listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".class");
		    }
		});
		assertNotNull("No *.class files for package name "+packageName+"?", files);
		assertFalse("No *.class files for package name "+packageName+"?",files.length < 1);
		List<Class<GlobalImmutableData>> classes = new ArrayList<>();
		// Find classes implementing GlobalImmutableData.
		for (File file : files) {
		    String className = file.getName().replaceAll(".class$", "");
		    Class<?> cls = null;
			try {
				cls = Class.forName(packageName + "." + className);
				if (GlobalImmutableData.class.isAssignableFrom(cls)) {
			    	classes.add((Class<GlobalImmutableData>) cls);
			    }
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		    
		}
		return classes;
	}
	
	private void error(Element e, String msg, Object... args) {
	    messager.printMessage(
	    	Kind.ERROR,
	    	String.format(msg, args),
	    	e);
	}
	private void warn(Element e, String msg, Object... args) {
		messager.printMessage(
		    	Kind.MANDATORY_WARNING,
		    	String.format(msg, args),
		    	e);
	}
	@Override
	public Set<String> getSupportedAnnotationTypes() { 
		return ImmutableSet.<String>of(
			TableMapping.class.getCanonicalName(),
			ColumnMapping.class.getCanonicalName()
		);
	}
	@Override
	public SourceVersion getSupportedSourceVersion() { return SourceVersion.latestSupported(); }
}
