package com.file_access_agent.common.util.json;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

import com.file_access_agent.common.util.location.LocationUtil;
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
        
        URI resourceURI = LocationUtil.computeResourceURI(src);

        recordJsonObject.add(
            "resourceURI",
            gson.toJsonTree(resourceURI)
        );

        Path resourcePath = LocationUtil.computeAbsolutePath(resourceURI);
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

    
    
    
}
