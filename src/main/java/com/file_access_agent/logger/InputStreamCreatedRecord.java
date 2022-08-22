package com.file_access_agent.logger;

import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InputStreamCreatedRecord extends RecordBase {

    protected InputStream inputStream;
    protected File file;
    protected FileDescriptor fileDescriptor;

    public InputStream getInputStream() {
        return inputStream;
    }

    public FileDescriptor getFileDescriptor() {
        return fileDescriptor;
    }

    public File getFile() {
        return file;
    }

    private InputStreamCreatedRecord(InputStream inputStream) {
        super();
        this.inputStream = inputStream;
    }

    public InputStreamCreatedRecord(InputStream inputStream, File file) {
        this(inputStream);
        this.file = file;
        this.fileDescriptor = null;
    }

    public InputStreamCreatedRecord(InputStream inputStream, FileDescriptor fileDescriptor) {
        this(inputStream);
        this.fileDescriptor = fileDescriptor;
        this.file = null;
    }

    @Override
    public void updateLists(AccessLogger accessLogger) {
        if (this.file != null) {
            Set<File> accessedFiles = accessLogger.getAccessedFiles();
            accessedFiles.add(this.file);
            Map<Closeable, File> inputStreamToFileMap = accessLogger.getInputStreamsToFilesMaps().get(InputStream.class.getName());
            inputStreamToFileMap.put(this.inputStream, this.file);

            AccessLogger.updateLogger(null, accessedFiles, null, Map.of(InputStream.class.getName(), inputStreamToFileMap), null);
        }
    }

    @Override
    public Map<String, String> getDebugInfo() {
        Map<String, String> debugInfo = new HashMap<>();

        debugInfo.put("record_type", "InputStreamCreatedRecord");
        debugInfo.put("InputStream", inputStream.toString());
        debugInfo.put("created_for_file", file.getAbsolutePath());

        return debugInfo;
    }
    
}
