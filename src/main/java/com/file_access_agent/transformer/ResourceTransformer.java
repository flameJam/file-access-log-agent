package com.file_access_agent.transformer;

import java.security.ProtectionDomain;

import com.file_access_agent.advice.ClassGetResourceAdvice;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

/** Transformer implementing the transformation of the getResource()-methods. */
public class ResourceTransformer implements AgentBuilder.Transformer {

    //@Override
    public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,
            JavaModule module, ProtectionDomain protectionDomain) {
        
        return getResourceMaybeAsStreamBuilder(builder);
    }

    @Override
    public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,
            JavaModule module) {
        
        return getResourceMaybeAsStreamBuilder(builder);
    }

    private Builder<?> getResourceMaybeAsStreamBuilder(Builder<?> builder) {

        return builder.visit(Advice.to(ClassGetResourceAdvice.class)
        .on(ElementMatchers.named("getResource")));
        // remove the following as soon as I am sure it is not necessary:
        // as far as I know "getResourceAsStream(...)" also uses "getResource" to locate a resource
        // which is convenient because I cannot collect the full path from an InputStream but only the URL
        // returned by getResource().
        // .or(ElementMatchers.named("getResourceAsStream"))));
    }
    
}
