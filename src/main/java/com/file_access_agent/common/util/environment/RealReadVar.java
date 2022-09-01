package com.file_access_agent.common.util.environment;

public class RealReadVar {

    private final static String REAL_READ_VAR_NAME = "FILE_ACCESS_AGENT_REAL_READ";

    public static boolean isDefined() {
        return System.getenv(REAL_READ_VAR_NAME) != null;
    }

    public static boolean isInRealReadMode() {
        return "true".equals(System.getenv(REAL_READ_VAR_NAME));
    }

}
