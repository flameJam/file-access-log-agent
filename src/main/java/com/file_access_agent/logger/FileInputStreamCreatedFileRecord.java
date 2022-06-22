package com.file_access_agent.logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Record storing a FileInputStream that was created using the given File */
public class FileInputStreamCreatedFileRecord extends FileInputStreamCreatedRecordBase {
    private File file;

    public File getFile() {
        return file;
    }

    public FileInputStreamCreatedFileRecord(FileInputStream fileInputStream, File file) {
        super(fileInputStream);
        this.file = file;
    }

    @Override
    public void updateLists(AccessLogger accessLogger) {
        Set<File> accessedFiles = accessLogger.getAccessedFiles();
        accessedFiles.add(this.file);
        Map<FileInputStream, File> fileInputStreamMap = accessLogger.getFileInputStreamMap();
        fileInputStreamMap.put(this.fileInputStream, this.file);
        AccessLogger.updateLogger(null, accessedFiles, null, fileInputStreamMap, null);
    }

    @Override
    public Map<String, String> getDebugInfo() {
        Map<String, String> debugInfo = new HashMap<>();

        debugInfo.put("record_type", "FileInputStreamCreatedFileRecord");
        debugInfo.put("FileInputStream", fileInputStream.toString());
        debugInfo.put("created_for_file", file.getAbsolutePath());

        return debugInfo;
    }
}