package com.gus.annotation.processor;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.junit.Test;

import com.google.common.collect.Iterables;

public class AnnotationProcessorTest {

    @Test
    public void runProcessor() throws Exception {
        Iterable<JavaFileObject> src = getSourceFiles("src");
        Iterable<JavaFileObject> test = getSourceFiles("test/unit/java");
        Iterable<JavaFileObject> cucumber = getSourceFiles("test/cucumber/java/innotas");
        Iterable<JavaFileObject> files = Iterables
                .unmodifiableIterable(Iterables.concat(src, test, cucumber));
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> options = new ArrayList<>();
        options.add("-source");
        options.add("1.8");
        options.add("-target");
        options.add("1.8");
        options.add("-s");
        options.add("generated_src");
        options.add("-nowarn");
        options.add("-proc:only");
        CompilationTask task = compiler.getTask(new PrintWriter(System.out), null, null, options,
                null, files);
        task.call();
    }

    public static void main(String[] args) throws Exception {
    	
    	AnnotationProcessorTest processor = new AnnotationProcessorTest();
    	processor.runProcessor();
    }
    
    private Iterable<JavaFileObject> getSourceFiles(String p_path) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager files = compiler.getStandardFileManager(null, null, null);

        files.setLocation(StandardLocation.SOURCE_PATH, Arrays.asList(new File(p_path)));

        Set<Kind> fileKinds = Collections.singleton(Kind.SOURCE);
        return files.list(StandardLocation.SOURCE_PATH, "", fileKinds, true);
    }
}