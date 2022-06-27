package com.file_access_agent.common.util.json;

import java.io.File;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/** JSON Serializer for Files */
public class FileSerializer implements JsonSerializer<File> {

    /**
     *  Serializes a File - since File is the type used to store information about accessed files in the AccessLogger it
     * does not only return the files toString(), but its name and absolute path
     */
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
