package com.file_access_agent.advice;

import java.net.URL;

import com.file_access_agent.common.util.environment.DebugVar;
import com.file_access_agent.logger.AccessLogger;

import net.bytebuddy.asm.Advice;

public class ImageIOLogReadURLAdvice {
    
    @Advice.OnMethodEnter
    public static void logImageInputStreamCreated(@Advice.Argument(0) URL url) {
        
            int recordId = AccessLogger.logResourceAcquired(url);
            if (DebugVar.isDebugModeTrue()) {
                AccessLogger.logStackTrace(recordId, Thread.currentThread().getStackTrace());
            }
    }

}
