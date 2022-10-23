package com.file_access_agent.advice;

import java.io.InputStream;
import java.nio.file.Path;

import com.file_access_agent.common.util.environment.DebugVar;
import com.file_access_agent.logger.AccessLogger;

import net.bytebuddy.asm.Advice;

public class FilesNewInputStreamAdvice {
    
    @Advice.OnMethodExit
    public static void logFilesNewInputStream(@Advice.Return InputStream inputStream,
    @Advice.Argument(0) Path firstArgumentPath) {
        //System.out.println("found Files.newInputStream(...)");
        int recordId = AccessLogger.logInputStreamCreatedWithPath(inputStream, firstArgumentPath);
        if (DebugVar.isDebugModeTrue()) {
            AccessLogger.logStackTrace(recordId, Thread.currentThread().getStackTrace());
        }
    }

}
