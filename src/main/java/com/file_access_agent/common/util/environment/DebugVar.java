package com.file_access_agent.common.util.environment;

public class DebugVar {
    private final static String AGENT_DEBUG_ENV_VAR_NAME = "FILE_ACCESS_AGENT_DEBUG";

    public static boolean isDebugModeTrue() {
        return "true".equals(System.getenv(AGENT_DEBUG_ENV_VAR_NAME));
    }

    public static boolean isDefined() {
        return System.getenv(AGENT_DEBUG_ENV_VAR_NAME) != null;
    }
}
