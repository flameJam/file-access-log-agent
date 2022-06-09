package com.file_access_agent.advice;

import com.file_access_agent.logger.AccessLogger;

import net.bytebuddy.asm.Advice;

public class MainMethodAdvice {

    private static final String OUTPUT_FILE_LOCATION="/home/jonas/Documents/test_output.txt";
    
    /** trigger the output calculation in the AccessLogger when the main methods is exiting. */
    // TODO this is not working in the Junit-Tests in Teamscale! Why?
    @Advice.OnMethodExit(onThrowable = Exception.class)
    public static void onExitingMain() {
        System.out.println("ON EXITING MAIN");
        AccessLogger.provideOutput(OUTPUT_FILE_LOCATION);
    }

}
