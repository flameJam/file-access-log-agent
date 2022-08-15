package com.file_access_agent.advice;

import java.io.File;

import javax.imageio.stream.ImageInputStream;

import com.file_access_agent.common.util.environment.DebugVar;
import com.file_access_agent.logger.AccessLogger;

import net.bytebuddy.asm.Advice;

public class ImageInputStreamCreatedFileLogAdvice {
    
    @Advice.OnMethodExit
    public static void logImageInputStreamCreated(@Advice.Return ImageInputStream imageInputStream, @Advice.Argument(0) Object firstArgumentObject) {
        if (firstArgumentObject instanceof File) {
            int recordId = AccessLogger.logFileImageInputStreamCreated(imageInputStream, (File) firstArgumentObject);
            if (DebugVar.isDebugModeTrue()) {
                AccessLogger.logStackTrace(recordId, Thread.currentThread().getStackTrace());
            }
        }
    }

}
