package com.file_access_agent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.util.jar.JarFile;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.ElementMatcher.Junction;
import net.bytebuddy.description.type.TypeDescription;

import com.file_access_agent.logger.AccessLogger;
import com.file_access_agent.transformer.ConstructorTransformer;
import com.file_access_agent.transformer.ReadTransformer;
import com.file_access_agent.transformer.ResourceTransformer;

/** Class containing the premain method required for java agents. */
public class FileAccessAgent {
    
    // Classes which are instrumented for dealing with java Files and FileInputStreams
    private final static String CLASSNAMES_TO_WATCH_FILE[] = {File.class.getName(), FileInputStream.class.getName()};

    // Classes whicha are instrumented for dealing with java Resources
    private final static String CLASSNAMES_TO_WATCH_RESOURCE[] = {Class.class.getName()};

    private static final String OUTPUT_FILE_LOCATION="/home/jonas/Documents/test_output.txt";

    /*
     * The premain method that is executed by the java agent before the main method of the instrumended application.
     * builds and installs all AgentBuilders necessary to watch for File-Accesses.
     * If the location of the jar of the logger-sub-package is not provided via the agent-args, it is expected to be found in
     * an environment variable "LoggerJarLocation".
     */
    public static void premain(String args, Instrumentation inst) throws IOException {

        // append the Logger-jar to the bootstrap class loader to make them available when using net.bytebuddy.asm.Advice for
        // instrumenting java classes.
        if (args != null) {
            inst.appendToBootstrapClassLoaderSearch(new JarFile(args));
        } else {
                String jarLocation = System.getenv("LoggerJarLocation");
                if (jarLocation == null) {
                    System.err.println("The location of the jar for the logger has to be specified as agent argument\n" +
                    "or as environment variable \"LoggerJarLocation\"!");
                    return;
                }
        }

        // Create the directory for storing jar files during bootclass injection
        final File tempFolder;
        try {
            tempFolder= Files.createTempDirectory("agent-bootstrap").toFile();
        } catch (Exception e) {
            System.err.println("Cannot create temp folder for bootstrap class instrumentation");
            e.printStackTrace(System.err);
            return;
        }

        getConstructorAgentBuilder(inst, tempFolder).installOn(inst);

        getReadAgentBuilder(inst, tempFolder).installOn(inst);

        getResourceAgentBuilder(inst, tempFolder).installOn(inst);


        addShutdownHookForOutputComputation();
    }

    /** Add a ShutdownHook to the JVM to trigger the output computation & generation */
    private static void addShutdownHookForOutputComputation() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                AccessLogger.provideOutput(OUTPUT_FILE_LOCATION);
            }
        });
    }

    /** AgentBuilder for instrumenting the Constructors of File-concerned classes. */
    private static AgentBuilder getConstructorAgentBuilder(Instrumentation inst, File tempFolder) {
        Junction<TypeDescription> typeMatcher = ElementMatchers.namedOneOf(CLASSNAMES_TO_WATCH_FILE);
        return getAgentBuilderTemplate(inst, tempFolder, typeMatcher).transform(new ConstructorTransformer());
    }

    /** AgentBuilder for instrumenting the read methods of the FileInputStream to log them. */
    private static AgentBuilder getReadAgentBuilder(Instrumentation inst, File tempFolder) {
        Junction<TypeDescription> typeMatcher = ElementMatchers.namedOneOf(CLASSNAMES_TO_WATCH_FILE);
        return getAgentBuilderTemplate(inst, tempFolder,typeMatcher).transform(new ReadTransformer());
    }

    /** AgentBuilder for instrumenting the Resource classes to log Resource retrieval. */
    private static AgentBuilder getResourceAgentBuilder(Instrumentation inst, File tempFolder) {
        Junction<TypeDescription> typeMatcher = ElementMatchers.namedOneOf(CLASSNAMES_TO_WATCH_RESOURCE);
        return getAgentBuilderTemplate(inst, tempFolder, typeMatcher).transform(new ResourceTransformer());
    }

    /** AgentBuilder Template reused in all other AgentBuilder-methods. */
    private static AgentBuilder.Identified.Narrowable getAgentBuilderTemplate(Instrumentation inst, File tempFolder, ElementMatcher<? super TypeDescription> typeMatcher) {
        return new AgentBuilder.Default()
        .disableClassFormatChanges()
        .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
        //.with(AgentBuilder.RedefinitionStrategy.Listener.StreamWriting.toSystemError())
        //.with(AgentBuilder.Listener.StreamWriting.toSystemError().withTransformationsOnly())
        .with(new AgentBuilder.InjectionStrategy.UsingInstrumentation(inst, tempFolder))
        //.with(AgentBuilder.InstallationListener.StreamWriting.toSystemError())
        .ignore(ElementMatchers.not(typeMatcher))
        .ignore(ElementMatchers.nameStartsWith("net.bytebuddy"))
        .type(typeMatcher);
    }

}
