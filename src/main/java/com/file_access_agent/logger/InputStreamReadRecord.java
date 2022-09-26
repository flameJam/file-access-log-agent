package com.file_access_agent.logger;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Record reading from a FileInputStream (currently unused) */
public class InputStreamReadRecord extends InputStreamRecordBase {

    public InputStreamReadRecord(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public void updateLists(AccessLogger accessLogger) {
        Set<File> readFiles = accessLogger.getReadFiles();
        Set<File> accessedFiles = accessLogger.getAccessedFiles();

        Map<Closeable, File> inputStreamToFileMap = accessLogger.getInputStreamsToFilesMaps().getOrDefault(inputStream.getClass().getName(), new HashMap<>());

        File readFile = inputStreamToFileMap.getOrDefault(inputStream, null);

        if (readFile != null && accessedFiles.contains(readFile)) {
            readFiles.add(readFile);
            AccessLogger.updateLogger(null, accessedFiles, null, null, null, readFiles);
        }
    }

    @Override
    public Map<String, String> getDebugInfo() {
        Map<String, String> debugInfo = new HashMap<>();

        debugInfo.put("record_type", "InputStreamReadRecord");
        debugInfo.put("InputStream", inputStream.toString());

        return debugInfo;
    }

}