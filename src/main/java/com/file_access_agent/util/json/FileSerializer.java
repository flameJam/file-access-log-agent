package com.file_access_agent.util.json;

import java.io.File;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class FileSerializer implements JsonSerializer<File> {

    @Override
    public JsonElement serialize(File src, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = JsonUtil.getGsonTemplate().create();
        if (src == null) {
            return gson.toJsonTree(null);
        }

        JsonObject fileObject = new JsonObject();

        fileObject.addProperty("name", src.getName());
        fileObject.addProperty("path", src.getAbsolutePath());

        return fileObject;
    }
    
}
