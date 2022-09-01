package com.file_access_agent.logger;

import java.io.InputStream;

public abstract class InputStreamRecordBase extends RecordBase {
    protected InputStream inputStream;

    public InputStream getInputStream() {
        return inputStream;
    }

    public InputStreamRecordBase(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}