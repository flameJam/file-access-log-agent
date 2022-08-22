package com.file_access_agent.logger;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Record storing a FileInputStream that was created using the given File */
public class FileInputStreamCreatedFileRecord extends InputStreamCreatedRecord {

    

    public FileInputStreamCreatedFileRecord(FileInputStream fileInputStream, File file) {
        super(fileInputStream, file);
    }

    @Override
    public void updateLists(AccessLogger accessLogger) {
        Set<File> accessedFiles = accessLogger.getAccessedFiles();
        accessedFiles.add(this.file);
        Map<Closeable, File> fileInputStreamMap = accessLogger.getInputStreamsToFilesMaps().get(FileInputStream.class.getName());
        fileInputStreamMap.put(this.inputStream, this.file);
        AccessLogger.updateLogger(null, accessedFiles, null, Map.of(FileInputStream.class.getName(), fileInputStreamMap), null);
    }

    @Override
    public Map<String, String> getDebugInfo() {
        Map<String, String> debugInfo = new HashMap<>();

        debugInfo.put("record_type", "FileInputStreamCreatedFileRecord");
        debugInfo.put("FileInputStream", inputStream.toString());
        debugInfo.put("created_for_file", file.getAbsolutePath());

        return debugInfo;
    }
}