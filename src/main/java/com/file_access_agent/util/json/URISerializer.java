package com.file_access_agent.util.json;

import java.lang.reflect.Type;
import java.net.URI;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/** JSON Serializer for URIs */
public class URISerializer implements JsonSerializer<URI> {

    @Override
    public JsonElement serialize(URI src, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = JsonUtil.getGsonTemplate().create();
        if (src != null) {
            return gson.toJsonTree(src.toString());
        }

        return gson.toJsonTree(null);
    }
    
}
