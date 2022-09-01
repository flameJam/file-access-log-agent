package com.file_access_agent.advice;

import java.io.FileInputStream;
import net.bytebuddy.asm.Advice;

public class InputStreamReadLogAdvice {

    /** log a read attempt on a FileInputStream */
    @Advice.OnMethodEnter()
    public static void logReadAttempt(@Advice.This FileInputStream fileInputStream) {
        
    }

}