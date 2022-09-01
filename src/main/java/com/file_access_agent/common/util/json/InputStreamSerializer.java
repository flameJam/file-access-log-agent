package com.file_access_agent.common.util.json;

import java.io.InputStream;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class InputStreamSerializer implements JsonSerializer<InputStream> {

    @Override
    public JsonElement serialize(InputStream src, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = JsonUtil.getGsonTemplate().create();
        if (src != null) {
            return gson.toJsonTree(src.toString());
        }

        return gson.toJsonTree(null);
    }
    
}
