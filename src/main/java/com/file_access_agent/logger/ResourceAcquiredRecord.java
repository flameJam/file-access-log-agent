package com.file_access_agent.logger;

import java.net.URL;
import java.util.Set;

/** Record storing a acquired resource -> URL */
public class ResourceAcquiredRecord extends RecordBase {
    private URL resourceURL;

    public URL getResourceURL() {
        return resourceURL;
    }

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