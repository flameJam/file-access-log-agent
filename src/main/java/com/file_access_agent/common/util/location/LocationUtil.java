package com.file_access_agent.common.util.location;

import java.io.IOError;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;

import com.file_access_agent.common.util.environment.RepositoryVar;

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


    public static Path computePath(URI resourceURI) {
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

        return resourcePath;
    }

    /**
     * Compute Path relative to the repository location
     * Assumes that the RepositoryVar is set
     * returns null if not!
     */
    public static Path getPathRelativeToRepo(Path resourcePath) {
        String repoPathString = RepositoryVar.getRepositoryPath();
        
        Path absoluteResourcePath = computeAbsolutePath(resourcePath);

        if (absoluteResourcePath == null || "".equals(repoPathString)) {
            return null;
        }

        try {
            return Path.of(repoPathString).toAbsolutePath().relativize(absoluteResourcePath);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /** Compute absolute Path from URI */
    public static Path computeAbsolutePath(Path resourcePath) {
        if (resourcePath == null) {
            return null;
        }

        String errorMessageTemplate = "Path %s could not be resolved to an absolute path - %s\n";
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
