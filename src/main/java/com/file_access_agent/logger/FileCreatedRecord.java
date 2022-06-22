package com.file_access_agent.logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public Map<String, String> getDebugInfo() {
        Map<String, String> debugInfo = new HashMap<>();

        debugInfo.put("record_type", "FileCreatedRecord");
        debugInfo.put("created_file", file.getAbsolutePath());

        return debugInfo;
    }
}