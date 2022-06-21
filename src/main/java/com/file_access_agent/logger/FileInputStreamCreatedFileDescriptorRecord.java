package com.file_access_agent.logger;

import java.io.FileDescriptor;
import java.io.FileInputStream;

/** Record storing a FileInputStream that was created using the given FileDescriptor (currently unused) */
public class FileInputStreamCreatedFileDescriptorRecord extends FileInputStreamCreatedRecordBase {
    private FileDescriptor fileDescriptor;

    public FileInputStreamCreatedFileDescriptorRecord(FileInputStream fileInputStream, FileDescriptor fileDescriptor) {
        super(fileInputStream);
        this.fileDescriptor = fileDescriptor;
    }

    @Override
    public void updateLists(AccessLogger accessLogger) {
        // TODO Is this Record even necessary?
        return;
    }

}
