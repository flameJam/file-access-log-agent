package com.file_access_agent.logger;

import java.io.FileDescriptor;
import java.io.FileInputStream;

public class FileInputStreamCreatedFileDescriptorRecord extends FileInputStreamCreatedRecordBase {
    private FileDescriptor fileDescriptor;

    public FileInputStreamCreatedFileDescriptorRecord(FileInputStream fileInputStream, FileDescriptor fileDescriptor) {
        super(fileInputStream);
        this.fileDescriptor = fileDescriptor;
    }

    @Override
    public AccessLogger updateLists(AccessLogger accessLogger) {
        // TODO Is this Record even necessary?
        return accessLogger;
    }

}
