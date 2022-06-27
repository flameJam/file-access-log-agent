package com.file_access_agent.common.util.environment;

import java.util.Arrays;

public class OutputFileVar {
    private static String OUTPUT_FILE_ENV_VAR_NAME = "FILE_ACCESS_AGENT_OUTPUT";

    public static boolean isDefined() {
        return System.getenv(OUTPUT_FILE_ENV_VAR_NAME) != null;
    }

    public static String getOutputPath() {
        if (!isDefined()) {
            return null;
        }

        String fullVar = System.getenv(OUTPUT_FILE_ENV_VAR_NAME);

        // different path delimiters for different OS: "/" for linux & MAC OS, "\" for Windows
        String pathParts[] = fullVar.split(System.getProperty("file.separator"));
        return String.join(System.getProperty("file.separator"), Arrays.copyOfRange(pathParts, 0, pathParts.length-1));
    }

    public static String getPrefix() {
        if (!isDefined()) {
            return null;
        }

        String pathParts[] = System.getenv(OUTPUT_FILE_ENV_VAR_NAME).split(System.getProperty("file.separator"));
        return pathParts[pathParts.length-1];
    }
}
