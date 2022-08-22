package com.file_access_agent.transformer;

import java.io.File;
import java.io.FileDescriptor;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import com.file_access_agent.advice.FileInputStreamConstructorLogAdvice;

/** Transformer implementing the transformation of the FileInputStream-Constructors */
public class ConstructorTransformer implements AgentBuilder.Transformer {

    @Override
    public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,
            JavaModule module) {

        /* //might reintroduce the transformation of the File-Constructor later, when implementing read-logging
        if("java.io.File".equals(typeDescription.getActualName())) {
            return getBuilderForFile(builder);
        }
        */

        if ("java.io.FileInputStream".equals(typeDescription.getActualName())) {
            return getBuilderForFileInputStream(builder);
        }

        /*if ("javax.imageio.stream.FileImageInputStream".equals(typeDescription.getActualName())) {
            return getBuilderForFileImageInputStream(builder);
        }*/

        return builder;

    }

    /*
    private Builder<?> getBuilderForFile(Builder<?> builder) {
        return builder
        .visit(Advice
        .to(FileConstructorLogAdvice.class)
        .on(ElementMatchers.isConstructor()));
    }
    */

    private Builder<?> getBuilderForFileInputStream(Builder<?> builder) {
        return builder
        .visit(
            Advice.to(FileInputStreamConstructorLogAdvice.class)
            .on(
                (ElementMatchers.isConstructor().and(ElementMatchers.takesArgument(0, File.class)
                .or(ElementMatchers.takesArgument(0, FileDescriptor.class)))))
        );
    }

    /*
    private Builder<?> getBuilderForFileImageInputStream(Builder<?> builder) {
        return builder
        .visit(
            Advice.to(FileImageInputStreamConstructorLogAdvice.class)
            .on(
                (ElementMatchers.isConstructor().and(ElementMatchers.takesArgument(0, File.class)
                )))
            );
    } */
    
}
