package com.file_access_agent.logger;

import java.io.File;

/** Record storing a File that was created (currently unused) */
public class FileCreatedRecord extends RecordBase {
    private File file;
    public FileCreatedRecord(File file) {
        super();
        this.file = file;
    }

    @Override
    public void updateLists(AccessLogger accessLogger) {
        return;
    }
}