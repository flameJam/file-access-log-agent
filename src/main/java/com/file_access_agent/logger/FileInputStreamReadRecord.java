package com.file_access_agent.logger;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public Map<String, String> getDebugInfo() {
        Map<String, String> debugInfo = new HashMap<>();

        debugInfo.put("record_type", "FileInputStreamReadRecord");
        debugInfo.put("FileInputStream", fileInputStream.toString());

        return debugInfo;
    }

}