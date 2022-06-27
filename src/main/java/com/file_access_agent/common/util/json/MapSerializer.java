package com.file_access_agent.common.util.json;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MapSerializer<K, V> implements JsonSerializer<Map<K, V>> {

    @Override
    public JsonElement serialize(Map<K, V> src, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = JsonUtil.getFullGsonBuilder()
            .create();

        if (src == null) {
            return gson.toJsonTree(null);
        }

        JsonObject recordJsonObject = new JsonObject();

        for (K key: src.keySet()) {
            recordJsonObject.add(key.toString(), gson.toJsonTree(src.get(key)));
        }

        return recordJsonObject;
    }
    
}
