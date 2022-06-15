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

public class URLSerializer implements JsonSerializer<URL> {

    @Override
    public JsonElement serialize(URL src, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = JsonUtil.getGsonTemplate().create();

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

        recordJsonObject.add(
            "absolute_path",
            gson.toJsonTree(resourcePath)
        );

        return recordJsonObject;
    }

    private void jsonComputationErrorMessage(String msg, Throwable throwable) {
        System.err.printf(msg);
        throwable.printStackTrace();
    }

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

    private Path computeAbsolutePath(URI resourceURI) {
        if (resourceURI == null) {
            return null;
        }

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
