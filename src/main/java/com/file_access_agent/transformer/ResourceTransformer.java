package com.file_access_agent.transformer;

import com.file_access_agent.advice.ClassGetResourceAdvice;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

/** Transformer implementing the transformation of the getResource()-methods. */
public class ResourceTransformer implements AgentBuilder.Transformer {

    @Override
    public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,
            JavaModule module) {
        
        return getResourceMaybeAsStreamBuilder(builder);
    }

    private Builder<?> getResourceMaybeAsStreamBuilder(Builder<?> builder) {

        return builder.visit(Advice.to(ClassGetResourceAdvice.class)
        .on(ElementMatchers.named("getResource").or(ElementMatchers.named("getResourceAsStream"))));
    }
    
}
