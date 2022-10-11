package com.file_access_agent.common.util.json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/** Class providing Util regarding json. Especially important: the method for generating an output json String for the AccessLogger */
public class JsonUtil {

    /** convert an Object to a Json String */
    public static String toJson(Object o) {
        return getFullGsonBuilder().create().toJson(o);
    }

    /** get the generally used Template for a Gson - no TypeAdapters registered! */
    public static GsonBuilder getGsonTemplate() {
        return new GsonBuilder().setPrettyPrinting().serializeNulls();
    }

    public static GsonBuilder getFullGsonBuilder() {
        return getGsonTemplate()
            .registerTypeAdapter(URL.class, new URLSerializer())
            .registerTypeAdapter(URI.class, new URISerializer())
            .registerTypeAdapter(Path.class, new PathSerializer())
            .registerTypeAdapter(File.class, new FileSerializer())
            .registerTypeAdapter(InputStream.class, new InputStreamSerializer())
            .registerTypeAdapter(Map.class, new MapSerializer<String, String>());
    }

    /** Compute a Json String containing the given timestamp, all accessed files and resources */
    public static String getOutputJsonString(Set<File> files, Set<URL> resources, List<String> recordDebugInfos, long testTimestamp) {
        Gson gson = getFullGsonBuilder().create();
        
        JsonObject accessLoggerObject = new JsonObject();

        // using the milliseconds since the beginning of 1970 as timestamp, similar to the implementation in Teamscale
        accessLoggerObject.add("testing_timestamp", gson.toJsonTree(Long.toString(testTimestamp)));

        // add a list of all accessed files
        accessLoggerObject.add("accessed_files", gson.toJsonTree(files));

        // add a list of all accessed resources
        accessLoggerObject.add("accessed_resources", gson.toJsonTree(resources));
        
        String jarEntryJsonArray = "[\n";
        for (URL url: resources) {
            if ("jar".equals(url.getProtocol())) {
                String jarEntryName = getJarEntryName(url);
                if (null != jarEntryName)
                jarEntryJsonArray += "{\"jar_entry\": \"" + jarEntryName + "\"},\n";
            }
        }
        jarEntryJsonArray += "\n]\n";
        accessLoggerObject.add("accessed_jar_entries", JsonParser.parseString(jarEntryJsonArray));

        List<JsonObject> recordDebugInfosJsonCopy = recordDebugInfos.stream()
            .map(jsonString -> JsonParser.parseString(jsonString).getAsJsonObject()).collect(Collectors.toList());



        if ("true".equals(System.getenv("FILE_ACCESS_AGENT_DEBUG")) || "specifics".equals(System.getenv("FILE_ACCESS_AGENT_DEBUG"))) {
            accessLoggerObject.add("record_debug_info", gson.toJsonTree(recordDebugInfosJsonCopy));
        }
        
        return gson.toJson(accessLoggerObject);
    }

    private static String getJarEntryName(URL url) {
        try {
            JarURLConnection jarUrlConnection = (JarURLConnection) url.openConnection();
            return jarUrlConnection.getEntryName();
        } catch (IOException e) {
            return null;
        }
    }

}
