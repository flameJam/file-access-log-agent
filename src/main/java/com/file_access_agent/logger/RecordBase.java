package com.file_access_agent.logger;

public abstract class RecordBase {
    private static int recordCounter = 0;
    protected int recordId;

    public RecordBase() {
        this.recordId = recordCounter++;
    }

    public abstract AccessLogger updateLists(AccessLogger accessLogger);

}
