package com.file_access_agent.logger;

import java.util.List;
import java.util.Map;

import com.file_access_agent.common.util.json.JsonUtil;

public class StackTraceRecord extends RecordBase {

    private int relatedRecordId;
    private StackTraceElement[] stackTrace;

    public StackTraceRecord(int relatedRecordId, StackTraceElement[] stackTrace) {
        super();
        this.relatedRecordId = relatedRecordId;
        this.stackTrace = stackTrace;
    }


    @Override
    public void updateLists(AccessLogger accessLogger) {
        
        List<String> stackTraceMap = accessLogger.getRecordDebugInfos();
        stackTraceMap.add(getRecordDebugInfo(accessLogger));

        AccessLogger.updateLogger(null, null, null, null, stackTraceMap);
    }

    private class DebugEntry {
        int recordId;
        Map<String, String> info;
        String stackTrace;
        
        public DebugEntry(int recordId, Map<String, String> info, String stackTrace) {
            this.recordId = recordId;
            this.info = info;
            this.stackTrace = stackTrace;
        }
    }

    private String getRecordDebugInfo(AccessLogger accessLogger) {
        DebugEntry debugEntry = new DebugEntry(relatedRecordId, accessLogger.getRecords().get(relatedRecordId).getDebugInfo(), getStackTraceString());
        return JsonUtil.toJson(debugEntry);
    }

    private String getStackTraceString() {
        String out = "";

        for (StackTraceElement element: stackTrace) {
            out += element.toString() + "\n";
        }

        return out;
    }


    @Override
    public Map<String, String> getDebugInfo() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
