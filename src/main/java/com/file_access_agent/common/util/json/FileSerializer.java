package com.file_access_agent.common.util.json;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Path;

import com.file_access_agent.common.util.location.LocationUtil;
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
        Path relativePath = getRelativePath(src);
        if (relativePath == null) {
            fileObject.add("path", gson.toJsonTree(null));    
        } else {
            fileObject.addProperty("path", getRelativePath(src).toString());
        }

        return fileObject;
    }

    private Path getRelativePath(File file) {
        Path filePath = file.toPath();
        return LocationUtil.getPathRelativeToRepo(filePath);
    }
    
}
