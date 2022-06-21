package com.file_access_agent.logger;

import java.io.FileInputStream;

/** Record reading from a FileInputStream (currently unused) */
public class FileInputStreamReadRecord extends FileInputStreamRecordBase{
    public FileInputStreamReadRecord(FileInputStream fileInputStream) {
        super(fileInputStream);
    }

    @Override
    public void updateLists(AccessLogger accessLogger) {
        // TODO Is this Record even necessary?
        return;
    }

}