package com.file_access_agent.logger;

import java.io.File;

public class FileCreatedRecord extends RecordBase {
    private File file;
    public FileCreatedRecord(File file) {
        super();
        this.file = file;
    }

    @Override
    public AccessLogger updateLists(AccessLogger accessLogger) {
        // TODO Auto-generated method stub
        return accessLogger;
    }
}