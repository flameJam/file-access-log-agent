package com.file_access_agent.advice;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;

import com.file_access_agent.common.util.environment.DebugVar;
import com.file_access_agent.logger.AccessLogger;

import net.bytebuddy.asm.Advice;

public class FileInputStreamConstructorLogAdvice {
    
    /** log the creation of a FileInputStream */
    @Advice.OnMethodExit
    public static void logInputStreamCreatedWithFile(@Advice.This FileInputStream fileInputStream,
     @Advice.Argument(0) Object firstArgumentObject) {
         
         if (firstArgumentObject instanceof File) {
            int recordId = AccessLogger.logFileInputStreamCreated(fileInputStream, (File) firstArgumentObject);
            if (DebugVar.isDebugModeTrue()) {
                AccessLogger.logStackTrace(recordId, Thread.currentThread().getStackTrace());
            }
        } else if (firstArgumentObject instanceof FileDescriptor) {
            // currently not used
            String msg = String.format("FileInputStream (%s) has just been created for FileDescriptor: %s\n", 
            fileInputStream.toString(),
            ((FileDescriptor) firstArgumentObject).toString());

            
            
            // Commented out to get to a first usable prototype
            //System.out.printf(msg);
        }
    }

}
