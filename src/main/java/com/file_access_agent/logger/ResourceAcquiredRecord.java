package com.file_access_agent.logger;

import java.util.Set;

public class ResourceAcquiredRecord extends RecordBase {
    private String resourceName;
    public ResourceAcquiredRecord(String resourceName) {
        super();
        this.resourceName = resourceName;
    }

    @Override
    public AccessLogger updateLists(AccessLogger accessLogger) {
        Set<String> accessedResources = accessLogger.getAccessedResources();
        accessedResources.add(this.resourceName);
        return new AccessLogger(accessLogger, null, null, accessedResources, null);
    }
}