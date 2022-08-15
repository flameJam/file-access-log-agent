package com.file_access_agent.advice;

import java.io.File;

import javax.imageio.stream.FileImageInputStream;

import com.file_access_agent.common.util.environment.DebugVar;
import com.file_access_agent.logger.AccessLogger;

import net.bytebuddy.asm.Advice;

public class FileImageInputStreamConstructorLogAdvice {
    
    /** log the creation of a FileImageInputStream */
    @Advice.OnMethodExit
    public static void logFileImageInputStreamCreatedWithFile(@Advice.This FileImageInputStream fileImageInputStream,
    @Advice.Argument(0) Object firstArgumentObject) {
        if (firstArgumentObject instanceof File) {
            int recordId = AccessLogger.logFileImageInputStreamCreated(fileImageInputStream, (File) firstArgumentObject);
            if (DebugVar.isDebugModeTrue()) {
                AccessLogger.logStackTrace(recordId, Thread.currentThread().getStackTrace());
            }
        }
    }
}
