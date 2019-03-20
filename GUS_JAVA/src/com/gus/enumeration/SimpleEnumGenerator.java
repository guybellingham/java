package com.gus.enumeration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import javax.lang.model.element.Modifier;

import org.apache.commons.lang3.StringUtils;

import com.gus.PrimaryKey;
import com.gus.annotation.TableMapping;
import com.gus.annotation.processor.GlobalImmutableData;
import com.opencsv.CSVReader;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
/**
 * This is a java application you can tweak and run to generate a 
 * {@link GlobalImmutableData} compliant Enum from its <code>seed_data</code> 
 * <code>.csv</code> file.
 * @author gus and ujjwal
 *
 */
public class SimpleEnumGenerator {

	/** Change these for every run. */
	private static final String enumName = "Timezone";
	private static final String tableName = "t_timezone";
	private static final String csvFilePath = "/seed_data/data/"+tableName+".csv";

	private static final String outputRootDir = "src";
	private static final String outputPackage = "tt.constants.globaldata";

	public static void main(String[] args) {
		System.out.println("EnumGenerator starting ...");
		TypeSpec typeSpec = loadContentFromCSVFile();

		JavaFile javaFile = JavaFile.builder(outputPackage, typeSpec).build();
		
		System.out.println("EnumGenerator writing "+javaFile.packageName+"."+javaFile.typeSpec.name);
		try {
			File file = new File(outputRootDir);
			javaFile.writeTo(file);

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("EnumGenerator done ...");
	}

    private static TypeSpec loadContentFromCSVFile() {
    	
    	TypeSpec typeSpec = null;
    	ClassName globalImmutableData = ClassName.get("com.innotas.globaldata", "GlobalImmutableData");
    	ClassName timezoneClass = ClassName.get("tt.constants.globaldata", enumName);
    	TypeName parameterizedClass = ParameterizedTypeName.get(globalImmutableData, timezoneClass);
 
    	String fileName = System.class.getResource(csvFilePath).getFile();
    	System.out.println("Reading csv file "+fileName);
    	
        try (FileReader fin = new FileReader(fileName)) {
            try (CSVReader cin = new CSVReader(fin, ',', '"', (char) 1)) {

                String[] headers = camelizeHeaders(cin.readNext());
                String[] rec = null;
                System.out.println("Got csv headings "+Arrays.toString(headers));
                
                StringBuilder sb = new StringBuilder(enumName);
                sb.append(" enum constants are synchronized with the ");
                sb.append(tableName.toUpperCase());
                sb.append("\n");
                sb.append("database table (signaled by the {@link TableMapping} annotation).\n");
                sb.append(enumName );
                sb.append(" constants contain the following:\n");
                for (int i = 0; i < headers.length; i++) {
                	sb.append("<li><code>");
                	sb.append(headers[i]);
                	sb.append("</code> - \n");
				}
                
                AnnotationSpec tableMapping = AnnotationSpec.builder(TableMapping.class)
            			.addMember("name", "$S", tableName.toUpperCase())
            			.build();
            	TypeSpec.Builder typeBuilder = TypeSpec.enumBuilder(enumName)
            			.addJavadoc(sb.toString())            			
        				.addModifiers(Modifier.PUBLIC)
        				.addSuperinterface(parameterizedClass)
        				.addAnnotation(tableMapping);
                MethodSpec.Builder methodBuilder = MethodSpec.constructorBuilder();

            	/**
            	 * Automated field name generation based on the headers.
            	 */
                // id field
			    typeBuilder.addField(long.class, "id", Modifier.PUBLIC, Modifier.FINAL);
			    methodBuilder.addParameter(long.class, headers[0]);
			    methodBuilder.addStatement("this.$N = $N", "id", headers[0]);
			    
			    for (int i = 1; i < headers.length; i++) {
    			    typeBuilder.addField(String.class, headers[i], Modifier.PUBLIC, Modifier.FINAL);
    			    methodBuilder.addParameter(String.class, headers[i]);
    			    methodBuilder.addStatement("this.$N = $N", headers[i], headers[i]);
            	}

            	typeBuilder.addMethod(methodBuilder.build());
            	typeBuilder.addMethod(buildGetPrimaryKeyMethod("id"));
            	typeBuilder.addMethod(buildGetDisplayTextMethod("title"));
            	typeBuilder.addMethod(buildForIdMethod(timezoneClass));
 
            	System.out.println("Reading csv records and generating Enum constants ...");
            	/**
            	 * Enum generation. Change the template String "$L, $L, $S, $L, $S, $S" 
            	 * and getEnumName() implementation as needed.
            	 */
                while ((rec = cin.readNext()) != null) {
                		typeBuilder.addEnumConstant(getEnumName(rec),
                				TypeSpec.anonymousClassBuilder("$L, $L, $S, $L, $S, $S",
                						rec[0], (rec[1] == "0") ? false : true, rec[2], null, rec[4], rec[5]).build());
                }
                
                typeSpec = typeBuilder.build();
            }
        }
        catch (IOException e) {
			e.printStackTrace();
        }
        
        return typeSpec;
    }
    /**
     * @param entry
     * @return a unique 'name' for each Enum constant based upon one 
     * or more Strings inside its csv record
     */
    private static String getEnumName(String[] entry) {
    	String enumName = entry[5].replaceAll("/", "_").replaceAll("-", "_");
    	return enumName.toUpperCase();
    }

    private static String[] camelizeHeaders(String[] headers) {
    	for (int i = 0; i < headers.length; i ++) {
        	String[] strings = StringUtils.split(headers[i].toLowerCase(), "_");
        	for (int j = 1; j < strings.length; j++) {
        		strings[j] = StringUtils.capitalize(strings[j]);
        	}
        	headers[i] = StringUtils.join(strings);
    	}
    	return headers;
    }
    /**
     * @param name
     * @return a getDisplayText() method that returns the given field <code>name</code>
     */
    private static MethodSpec buildGetDisplayTextMethod(String name) {
    	return MethodSpec.methodBuilder("getDisplayText")
    			.addModifiers(Modifier.PUBLIC)
    			.returns(String.class)
    			.addStatement("return $L", name)
    			.addAnnotation(java.lang.Override.class)
    			.build();
    }
    /**
     * @param name
     * @return a getPrimaryKey() method that returns 
     * a PrimaryKey built around the given field <code>name</code> 
     */
    private static MethodSpec buildGetPrimaryKeyMethod(String name) {
    	return MethodSpec.methodBuilder("getPrimaryKey")
    			.addModifiers(Modifier.PUBLIC)
    			.returns(PrimaryKey.class)
    			.addStatement("return $T.getPrimaryKey($L)", GlobalImmutableData.class, name)
    			.addAnnotation(java.lang.Override.class)
    			.build();
    }
    /**
     * @param timezoneClass - the Enum class being generated
     * @return a <code>forId(long id)</code> method
     */
    private static MethodSpec buildForIdMethod(ClassName timezoneClass) {
    	return MethodSpec.methodBuilder("forId")
    			.addModifiers(Modifier.PUBLIC)
    			.returns(timezoneClass)
    			.addParameter(long.class, "id")
    			.addStatement("return $T.forId($T.getPrimaryKey(id), $T.class)", 
    					GlobalImmutableData.class, GlobalImmutableData.class, timezoneClass)
    			.build();
    }
}
