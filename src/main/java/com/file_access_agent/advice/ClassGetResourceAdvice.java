package com.file_access_agent.advice;

import java.net.URL;
import com.file_access_agent.logger.AccessLogger;

import net.bytebuddy.asm.Advice;

public class ClassGetResourceAdvice {

    /** Log resource retrieval on method exit */
    @Advice.OnMethodExit()
    public static void onResourceWanted(@Advice.Return(readOnly = true) URL resourceURL) {
        if (resourceURL != null) {
            AccessLogger.logResourceAcquired(resourceURL);
        }
    }

}