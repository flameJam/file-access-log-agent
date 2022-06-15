package com.file_access_agent.logger;

import java.io.IOError;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;
import java.util.Set;

public class ResourceAcquiredRecord extends RecordBase {
    private URL resourceURL;

    public URL getResourceURL() {
        return resourceURL;
    }

    public URI getResourceURI() {
        return resourceURI;
    }

    public Path getPath() {
        return path;
    }

    private URI resourceURI = null;

    private Path path = null;

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

    public void getJsonReady() {
        try {
            resourceURI = resourceURL.toURI();
        } catch (URISyntaxException uriSyntaxExc) {
            jsonComputationErrorMessage(String.format("URL %s could not be resolved to an URI - URISyntaxException:\n", resourceURL), uriSyntaxExc);
            uriSyntaxExc.printStackTrace();
            return;
        }

        Path resourcePath = null;
        String errorMessageTemplate = "URI %s could not be resolved to a path - %s\n";
        try {
            resourcePath = Path.of(resourceURI);
        } catch (IllegalArgumentException illArgExc) {
            jsonComputationErrorMessage(String.format(errorMessageTemplate, resourceURI, "IllegalArgumentException"), illArgExc);
            return;
        } catch (FileSystemNotFoundException fileSysNotFoundExc) {
            jsonComputationErrorMessage(String.format(errorMessageTemplate, resourceURI, "FileSystemNotFoundException"), fileSysNotFoundExc);
            return;
        } catch (SecurityException securityException) {
            jsonComputationErrorMessage(String.format(errorMessageTemplate, resourceURI, "SecurityException"), securityException);
            return;
        }

        errorMessageTemplate = "Path %s could not be resolved to an absolute path - %s\n";
        try {
            resourcePath = resourcePath.toAbsolutePath();
        } catch (SecurityException securityException) {
            jsonComputationErrorMessage(String.format(errorMessageTemplate, resourcePath, "SecurityException"), securityException);
            return;
        } catch (IOError ioError) {
            jsonComputationErrorMessage(String.format(errorMessageTemplate, resourcePath, "IOError"), ioError);
            return;
        }

        path = resourcePath;
    }

    private void jsonComputationErrorMessage(String msg, Throwable throwable) {
        System.err.printf(msg, resourceURI);
        throwable.printStackTrace();
        return;
    }

}