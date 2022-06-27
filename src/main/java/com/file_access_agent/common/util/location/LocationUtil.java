package com.file_access_agent.common.util.location;

import java.io.IOError;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;

public class LocationUtil {

    /** Compute URI from URL */
    public static URI computeResourceURI(URL resourceURL) {
        if (resourceURL == null) {
            return null;
        }
        URI resourceURI= null;
        try {
            resourceURI = resourceURL.toURI();
        } catch (URISyntaxException uriSyntaxExc) {
            locationComputationErrorMessage(String.format("URL %s could not be resolved to an URI - URISyntaxException:\n", resourceURL), uriSyntaxExc);
            uriSyntaxExc.printStackTrace();
        }

        return resourceURI;
    }


    /** Compute absolute Path from URI */
    public static Path computeAbsolutePath(URI resourceURI) {
        if (resourceURI == null) {
            return null;
        }

        // get the path which still might be relative
        Path resourcePath = null;
        String errorMessageTemplate = "URI %s could not be resolved to a path - %s\n";
        try {
            resourcePath = Path.of(resourceURI);
        } catch (IllegalArgumentException illArgExc) {
            locationComputationErrorMessage(String.format(errorMessageTemplate, resourceURI, "IllegalArgumentException"), illArgExc);
            return null;
        } catch (FileSystemNotFoundException fileSysNotFoundExc) {
            locationComputationErrorMessage(String.format(errorMessageTemplate, resourceURI, "FileSystemNotFoundException"), fileSysNotFoundExc);
            return null;
        } catch (SecurityException securityException) {
            locationComputationErrorMessage(String.format(errorMessageTemplate, resourceURI, "SecurityException"), securityException);
            return null;
        }

        // get the absolute path
        errorMessageTemplate = "Path %s could not be resolved to an absolute path - %s\n";
        try {
            resourcePath = resourcePath.toAbsolutePath();
        } catch (SecurityException securityException) {
            locationComputationErrorMessage(String.format(errorMessageTemplate, resourcePath, "SecurityException"), securityException);
            return null;
        } catch (IOError ioError) {
            locationComputationErrorMessage(String.format(errorMessageTemplate, resourcePath, "IOError"), ioError);
            return null;
        }

        return resourcePath;
    }

    private static void locationComputationErrorMessage(String msg, Throwable throwable) {
        System.out.printf(msg);
        throwable.printStackTrace();
    }
}
