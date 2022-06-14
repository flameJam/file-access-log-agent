package com.file_access_agent.logger;

import java.net.URL;
import java.util.Set;

public class ResourceAcquiredRecord extends RecordBase {
    private URL resourceURL;
    public ResourceAcquiredRecord(URL resourceURL) {
        super();
        this.resourceURL = resourceURL;
    }

    @Override
    public AccessLogger updateLists(AccessLogger accessLogger) {
        Set<URL> accessedResources = accessLogger.getAccessedResources();
        accessedResources.add(this.resourceURL);
        return new AccessLogger(accessLogger, null, null, accessedResources, null);
    }
}