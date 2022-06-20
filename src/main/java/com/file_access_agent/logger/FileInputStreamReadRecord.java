package com.file_access_agent.logger;

import java.io.FileInputStream;

/** Record reading from a FileInputStream (currently unused) */
public class FileInputStreamReadRecord extends FileInputStreamRecordBase{
    public FileInputStreamReadRecord(FileInputStream fileInputStream) {
        super(fileInputStream);
    }

    @Override
    public AccessLogger updateLists(AccessLogger accessLogger) {
        // TODO add if we add "read" to statuses of files
        return accessLogger;
    }
}