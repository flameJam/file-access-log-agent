package com.file_access_agent.advice;

import java.io.InputStream;

import com.file_access_agent.common.util.environment.DebugVar;
import com.file_access_agent.logger.AccessLogger;

import net.bytebuddy.asm.Advice;

public class InputStreamReadLogAdvice {

    /** log a read attempt on a FileInputStream */
    @Advice.OnMethodEnter()
    public static void logReadAttempt(@Advice.This InputStream inputStream) {
        int recordId = AccessLogger.logInputStreamRead(inputStream);
        if (DebugVar.isDefined() && DebugVar.isDebugModeTrue()) {
            AccessLogger.logStackTrace(recordId, Thread.currentThread().getStackTrace());
        }
    }

}