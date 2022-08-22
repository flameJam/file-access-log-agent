package com.file_access_agent.logger;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.stream.ImageInputStream;

// this does not extend InputStreamCreated because ImageInputStream is an interface and not a subtype of InputStream
public class ImageInputStreamCreatedFileRecord extends RecordBase {

    private ImageInputStream imageInputStream;

    private File file;

    public ImageInputStreamCreatedFileRecord(ImageInputStream imageInputStream) {
        super();
        this.imageInputStream = imageInputStream;
    }

    public ImageInputStreamCreatedFileRecord(ImageInputStream imageInputStream, File file) {
        this(imageInputStream);
        this.file = file;
    }

    @Override
    public void updateLists(AccessLogger accessLogger) {
        Set<File> accessedFiles= accessLogger.getAccessedFiles();
        accessedFiles.add(this.file);
        Map<Closeable, File> imageInputStreamMap = accessLogger.getInputStreamsToFilesMaps().get(ImageInputStream.class.getName());
        imageInputStreamMap.put(this.imageInputStream, this.file);
        AccessLogger.updateLogger(null, accessedFiles, null, Map.of(ImageInputStream.class.getName(), imageInputStreamMap), null);
    }

    @Override
    public Map<String, String> getDebugInfo() {
        Map<String, String> debugInfo = new HashMap<>();

        debugInfo.put("record_type", "ImageInputStreamCreatedFileRecord");
        debugInfo.put("ImageInputStream", imageInputStream.toString());
        debugInfo.put("created_for_file", file.getAbsolutePath());

        return debugInfo;
    }
}
