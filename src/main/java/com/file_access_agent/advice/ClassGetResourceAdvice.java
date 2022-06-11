package com.file_access_agent.advice;

import com.file_access_agent.logger.AccessLogger;

import net.bytebuddy.asm.Advice;

public class ClassGetResourceAdvice {

    /** Log resource retrieval on method exit */
    @Advice.OnMethodExit()
    public static void onResourceWanted(@Advice.Origin("#m") String methodName, @Advice.Origin("#t") String type,
    @Advice.Argument(0) String resourceName) {
        // TODO find a way to get the actual location of the resource
        // idea: via return value -> get URI -> get File -> get absolute path?
        AccessLogger.logResourceAcquired(resourceName);
    }

}