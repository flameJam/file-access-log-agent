package com.file_access_agent.transformer;

import java.security.ProtectionDomain;

import com.file_access_agent.advice.InputStreamReadLogAdvice;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

/** Transformer implementing the transformation of the FileInputStream.read(...) methods */
public class ReadTransformer implements AgentBuilder.Transformer{

    //@Override
    public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,
            JavaModule module, ProtectionDomain protectionDomain) {
        return getBuilderInputStream(builder);
    }

    @Override
    public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,
            JavaModule module) {
        return getBuilderInputStream(builder);
    }

    private Builder<?> getBuilderInputStream(Builder<?> builder) {
        return builder
        .visit(Advice.to(InputStreamReadLogAdvice.class)
        .on(ElementMatchers.named("read").or(ElementMatchers.named("readBytes"))));
    }
    
}
