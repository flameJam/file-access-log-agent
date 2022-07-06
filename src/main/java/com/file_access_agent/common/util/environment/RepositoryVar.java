package com.file_access_agent.common.util.environment;

public class RepositoryVar {
    private final static String AGENT_REPOSITORY_ENV_VAR_NAME = "FILE_ACCESS_AGENT_REPOSITORY";

    public static boolean isDefined() {
        return System.getenv(AGENT_REPOSITORY_ENV_VAR_NAME) != null;
    }

    public static String getRepositoryPath() {
        if (isDefined()) {
            return System.getenv(AGENT_REPOSITORY_ENV_VAR_NAME);
        }

        return "";
    }
}