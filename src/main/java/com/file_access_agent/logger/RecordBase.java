package com.file_access_agent.logger;

/** Abstract record storing some kind of interesting information on accessed Files, Resources, FileInputStreams... */
public abstract class RecordBase {
    private static int recordCounter = 0;
    protected int recordId;

    public RecordBase() {
        this.recordId = recordCounter++;
    }

    // abstract method to update the lists inside an AccessLogger
    public abstract AccessLogger updateLists(AccessLogger accessLogger);

}
