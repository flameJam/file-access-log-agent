package com.file_access_agent.util.json;

import java.io.IOError;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/** Serializer for URLs */
public class URLSerializer implements JsonSerializer<URL> {

    /**
     * Since the URL is the type used for storing resources, this does not only return the string of the URL,
     * but also its URI and absolute path (if possible)
    */
    @Override
    public JsonElement serialize(URL src, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = JsonUtil.getGsonTemplate()
        .registerTypeAdapter(URI.class, new URISerializer())
        .registerTypeAdapter(Path.class, new PathSerializer())
        .create();

        if (src == null) {
            return gson.toJsonTree(null);
        }

        JsonObject recordJsonObject = new JsonObject();
        
        recordJsonObject.addProperty(
            "resourceURL", src.toString()
            );
        
        URI resourceURI = computeResourceURI(src);

        recordJsonObject.add(
            "resourceURI",
            gson.toJsonTree(resourceURI)
        );

        Path resourcePath = computeAbsolutePath(resourceURI);
        if (resourcePath == null) {
            recordJsonObject.add(
            "absolutePath",
            gson.toJsonTree(null)
            );
        } else {
            // TODO why does the PathSerializer not work! >___<
            recordJsonObject.add(
                "absolutePath",
                gson.toJsonTree(resourcePath.toString())
            );
        }

        return recordJsonObject;
    }

    private void jsonComputationErrorMessage(String msg, Throwable throwable) {
        System.out.printf(msg);
        throwable.printStackTrace();
    }

    /** Compute URI from URL */
    private URI computeResourceURI(URL resourceURL) {
        if (resourceURL == null) {
            return null;
        }
        URI resourceURI= null;
        try {
            resourceURI = resourceURL.toURI();
        } catch (URISyntaxException uriSyntaxExc) {
            jsonComputationErrorMessage(String.format("URL %s could not be resolved to an URI - URISyntaxException:\n", resourceURL), uriSyntaxExc);
            uriSyntaxExc.printStackTrace();
        }

        return resourceURI;
    }

    /** Compute absolute Path from URI */
    private Path computeAbsolutePath(URI resourceURI) {
        if (resourceURI == null) {
            return null;
        }

        // get the path which still might be relative
        Path resourcePath = null;
        String errorMessageTemplate = "URI %s could not be resolved to a path - %s\n";
        try {
            resourcePath = Path.of(resourceURI);
        } catch (IllegalArgumentException illArgExc) {
            jsonComputationErrorMessage(String.format(errorMessageTemplate, resourceURI, "IllegalArgumentException"), illArgExc);
            return null;
        } catch (FileSystemNotFoundException fileSysNotFoundExc) {
            jsonComputationErrorMessage(String.format(errorMessageTemplate, resourceURI, "FileSystemNotFoundException"), fileSysNotFoundExc);
            return null;
        } catch (SecurityException securityException) {
            jsonComputationErrorMessage(String.format(errorMessageTemplate, resourceURI, "SecurityException"), securityException);
            return null;
        }

        // get the absolute path
        errorMessageTemplate = "Path %s could not be resolved to an absolute path - %s\n";
        try {
            resourcePath = resourcePath.toAbsolutePath();
        } catch (SecurityException securityException) {
            jsonComputationErrorMessage(String.format(errorMessageTemplate, resourcePath, "SecurityException"), securityException);
            return null;
        } catch (IOError ioError) {
            jsonComputationErrorMessage(String.format(errorMessageTemplate, resourcePath, "IOError"), ioError);
            return null;
        }

        return resourcePath;
    }
    
}
