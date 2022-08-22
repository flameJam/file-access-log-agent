package com.file_access_agent.transformer;

import java.nio.file.Path;

import com.file_access_agent.advice.FilesNewInputStreamAdvice;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

public class FilesTransformer implements AgentBuilder.Transformer {

    @Override
    public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,
            JavaModule module) {
        return builder
        .visit(Advice.to(FilesNewInputStreamAdvice.class)
        .on(
            ElementMatchers.named("newInputStream")
            .and(
                ElementMatchers.takesArgument(0, Path.class)
                )
            )
        );
    }
    
}
