package com.file_access_agent.advice;

import java.io.File;
import net.bytebuddy.asm.Advice;

public class FileConstructorLogAdvice {

    /** Log File Creation (currently not used) */
    @Advice.OnMethodExit
    public static void printFilePath(@Advice.This File file) {
        System.out.println(((File)file).getAbsolutePath() + " was opened");
    }

}