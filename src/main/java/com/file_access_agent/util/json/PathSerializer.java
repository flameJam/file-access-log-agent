package com.file_access_agent.util.json;

import java.lang.reflect.Type;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PathSerializer implements JsonSerializer<Path> {

    @Override
    public JsonElement serialize(Path src, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = JsonUtil.getGsonTemplate().create();
        if (src != null) {
            return gson.toJsonTree(src.toString());
        }
        return gson.toJsonTree(null);
    }
    
}
