package com.file_access_agent.logger;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

        public String getJsonString() {
            String json = "{\n";
            json += "\"recordId\": " + this.recordId + ",\n";
            json += "\"info\":" + getJsonStringForMap(this.info) + ",\n";
            json += "\"stackTrace\": \"" + this.stackTrace + "\"\n}\n";
            return json;
        }

        private String getJsonStringForMap(Map<String, String> map) {
            String json = "{\n";
            
            boolean enteredLoop = false;
            for (Entry<String, String> entry: map.entrySet()) {
                enteredLoop = true;
                json += "\"" + entry.getKey() + "\": \"" + entry.getValue() + "\",\n";
            }
            if (enteredLoop) {
                json = json.substring(0, json.length()-2);
            }

            json += "}";

            return json;
        }
    }

    private String getRecordDebugInfo(AccessLogger accessLogger) {
        DebugEntry debugEntry = new DebugEntry(relatedRecordId, accessLogger.getRecords().get(relatedRecordId).getDebugInfo(), getStackTraceString());
        return debugEntry.getJsonString();
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
        return null;
    }
    
}
