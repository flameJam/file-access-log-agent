package com.file_access_agent.util.json;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/** Class providing Util regarding json. Especially important: the method for generating an output json String for the AccessLogger */
public class JsonUtil {

    /** convert an Object to a Json String */
    public static String toJson(Object o) {
        return getGsonTemplate()
            .registerTypeAdapter(URL.class, URLSerializer.class)
            .registerTypeAdapter(URI.class, URISerializer.class)
            .registerTypeAdapter(Path.class, PathSerializer.class)
            .registerTypeAdapter(File.class, FileSerializer.class)
            .create()
            .toJson(o);
    }

    /** get the generally used Template for a Gson - no TypeAdapters registered! */
    public static GsonBuilder getGsonTemplate() {
        return new GsonBuilder()
        .setPrettyPrinting()
        .excludeFieldsWithoutExposeAnnotation()
        .serializeNulls();
    }

    /** Compute a Json String containing the given timestamp, all accessed files and resources */
    public static String getOutputJsonString(Set<File> files, Set<URL> resources, long testTimestamp) {
        Gson gson = JsonUtil.getGsonTemplate()
            .registerTypeAdapter(URL.class, new URLSerializer())
            .registerTypeAdapter(URI.class, new URISerializer())
            .registerTypeAdapter(Path.class, new PathSerializer())
            .registerTypeAdapter(File.class, new FileSerializer())
            .create();
        
        JsonObject accessLoggerObject = new JsonObject();

        // using the milliseconds since the beginning of 1970 as timestamp, similar to the implementation in Teamscale
        accessLoggerObject.add("testing_timestamp", gson.toJsonTree(Long.toString(testTimestamp)));

        // add a list of all accessed files
        accessLoggerObject.add("accessed_files", gson.toJsonTree(files));

        // add a list of all accessed resources
        accessLoggerObject.add("accessed_resources", gson.toJsonTree(resources));
        
        return gson.toJson(accessLoggerObject);
    }

}
