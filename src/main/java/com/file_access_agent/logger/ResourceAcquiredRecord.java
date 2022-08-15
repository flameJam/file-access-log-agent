package com.file_access_agent.logger;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.file_access_agent.common.util.json.URLSerializer;
import com.file_access_agent.common.util.location.LocationUtil;

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
    public void updateLists(AccessLogger accessLogger) {
        Set<URL> accessedResources = accessLogger.getAccessedResources();
        accessedResources.add(this.resourceURL);
        AccessLogger.updateLogger(null, null, accessedResources, null, null, null);
    }

    @Override
    public Map<String, String> getDebugInfo() {
        Map<String, String> debugInfo = new HashMap<>();

        debugInfo.put("record_type", "ResourceAcquiredRecord");

        debugInfo.put("URL", resourceURL.toString());

        URI resourceURI = LocationUtil.computeResourceURI(resourceURL);
        if (resourceURI != null) {
            debugInfo.put("URI", resourceURI.toString());
        } else {
            debugInfo.put("URI", null);
        }
        
        Path resourcePath = LocationUtil.computePath(resourceURI);
        resourcePath = LocationUtil.getPathRelativeToRepo(resourcePath);
        if (resourcePath != null) {
            debugInfo.put("absolute_path", resourcePath.toString());
        } else {
            debugInfo.put("absolute_path", null);
        }
        
        return debugInfo;
    }
}