package com.file_access_agent.transformer;

import com.file_access_agent.advice.MainMethodAdvice;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

/** Transformer implementing the transformation of the Main-Method */
public class MainTransformer implements AgentBuilder.Transformer {

    @Override
    public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,
            JavaModule module) {
        
        return getBuilderForMainMethod(builder);
    }

    private Builder<?> getBuilderForMainMethod(Builder<?> builder) {
        return builder
        .visit(
            Advice.to(MainMethodAdvice.class)
            .on(ElementMatchers.isMain())
        );
    }
    
}
