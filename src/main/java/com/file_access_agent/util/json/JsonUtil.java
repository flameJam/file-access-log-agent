package com.file_access_agent.util.json;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class JsonUtil {

    public static String toJson(Object o) {
        return getGsonTemplate()
            .registerTypeAdapter(URL.class, URLSerializer.class)
            .registerTypeAdapter(URI.class, URISerializer.class)
            .registerTypeAdapter(Path.class, PathSerializer.class)
            .registerTypeAdapter(File.class, FileSerializer.class)
            .create()
            .toJson(o);
    }

    public static GsonBuilder getGsonTemplate() {
        return new GsonBuilder()
        .setPrettyPrinting()
        .excludeFieldsWithoutExposeAnnotation()
        .serializeNulls();
    }

    public static String getOutputJsonString(Set<File> files, Set<URL> resources) {
        Gson gson = JsonUtil.getGsonTemplate()
            .registerTypeAdapter(URL.class, new URLSerializer())
            .registerTypeAdapter(URI.class, new URISerializer())
            .registerTypeAdapter(Path.class, new PathSerializer())
            .registerTypeAdapter(File.class, new FileSerializer())
            .create();
        
        JsonObject accessLoggerObject = new JsonObject();

        accessLoggerObject.add("accessed_files", gson.toJsonTree(files));

        accessLoggerObject.add("accessed_resources", gson.toJsonTree(resources));
        return gson.toJson(accessLoggerObject);
    }

}
