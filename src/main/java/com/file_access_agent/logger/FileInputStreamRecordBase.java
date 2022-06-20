package com.file_access_agent.logger;

import java.io.FileInputStream;

/** abstract record concerning the given FileInputStream */
public abstract class FileInputStreamRecordBase extends RecordBase {
    protected FileInputStream fileInputStream;
    public FileInputStreamRecordBase(FileInputStream fileInputStream) {
        super();
        this.fileInputStream = fileInputStream;
    }
}