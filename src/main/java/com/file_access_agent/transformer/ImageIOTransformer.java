package com.file_access_agent.transformer;

import java.net.URL;
import java.security.ProtectionDomain;

import com.file_access_agent.advice.ImageInputStreamCreatedFileLogAdvice;
import com.file_access_agent.advice.ImageIOLogReadURLAdvice;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

public class ImageIOTransformer implements AgentBuilder.Transformer {

    public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,
            JavaModule module, ProtectionDomain protectionDomain) {
        
        return builder
        .visit(Advice.to(ImageInputStreamCreatedFileLogAdvice.class)
        .on(
            ElementMatchers.isMethod().and(ElementMatchers.named("createImageInputStream"))
        )).visit(Advice.to(ImageIOLogReadURLAdvice.class).on(
            ElementMatchers.isMethod()
            .and(ElementMatchers.named("read")
            .and(ElementMatchers.takesArgument(0, URL.class)))));
    }

    @Override
    public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,
            JavaModule module) {
        
        return builder
        .visit(Advice.to(ImageInputStreamCreatedFileLogAdvice.class)
        .on(
            ElementMatchers.isMethod().and(ElementMatchers.named("createImageInputStream"))
        )).visit(Advice.to(ImageIOLogReadURLAdvice.class).on(
            ElementMatchers.isMethod()
            .and(ElementMatchers.named("read")
            .and(ElementMatchers.takesArgument(0, URL.class)))));
    }
    
}
