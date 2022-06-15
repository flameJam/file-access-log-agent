package com.file_access_agent.logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Set;

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
    public AccessLogger updateLists(AccessLogger accessLogger) {
        Set<File> accessedFiles = accessLogger.getAccessedFiles();
        accessedFiles.add(this.file);
        Map<FileInputStream, File> fileInputStreamMap = accessLogger.getFileInputStreamMap();
        fileInputStreamMap.put(this.fileInputStream, this.file);
        return new AccessLogger(accessLogger, null, accessedFiles, null, fileInputStreamMap);
    }
}