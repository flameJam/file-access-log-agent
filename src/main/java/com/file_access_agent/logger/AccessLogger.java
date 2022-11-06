package com.file_access_agent.logger;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.imageio.stream.ImageInputStream;

/** 
 * Class used during in instrumentation code to log the access to different resources, files,...
 * Can "keep state" and compute dependencies between different "log-entries"
 * but does all these computations only when told to compute & generate the outputfile
 * in order to impair the instrumented target's performance as little as possible.
 */
public class AccessLogger {

    /** the used AccessLogger Instance, used to implement a Singleton */
    private static AccessLogger ACCESS_LOGGER;

    public Set<File> getReadFiles() {
        return readFiles;
    }

    /** the List of Records recorded/logged during testing*/
    private Map<Integer, RecordBase> records;

    /** the set of accessed files */
    private Set<File> accessedFiles;

    /** the set of read files */
    private Set<File> readFiles;

    /** the list of access resources -> URLs */
    private Set<URL> accessedResources;
    
    /** a map mapping different types of inputStreams to a map mapping distinct instances of those streams to the files they open & read */
    private Map<String, Map<Closeable, File>> inputStreamsToFilesMaps;

    public Map<String, Map<Closeable, File>> getInputStreamsToFilesMaps() {
        return inputStreamsToFilesMaps;
    }

    private List<String> recordDebugInfos;

    public List<String> getRecordDebugInfos() {
        return recordDebugInfos;
    }

    public Map<Integer, RecordBase> getRecords() {
        return records;
    }

    public Set<File> getAccessedFiles() {
        return accessedFiles;
    }

    public Set<URL> getAccessedResources() {
        return accessedResources;
    }
 
    /** empty constructor */
    private AccessLogger() {

        records = new HashMap<>();
        accessedFiles = new HashSet<>();
        accessedResources = new HashSet<>();
        readFiles = new HashSet<>();

        inputStreamsToFilesMaps = new HashMap<>();
        inputStreamsToFilesMaps.put(FileInputStream.class.getName(), new HashMap<>());
        inputStreamsToFilesMaps.put(ImageInputStream.class.getName(), new HashMap<>());
        inputStreamsToFilesMaps.put(InputStream.class.getName(), new HashMap<>());

        recordDebugInfos = new ArrayList<>();
    }

    private AccessLogger(AccessLogger oldVersion) {
        records = oldVersion.getRecords();
        accessedFiles = oldVersion.getAccessedFiles();
        accessedResources = oldVersion.getAccessedResources();
        inputStreamsToFilesMaps = oldVersion.getInputStreamsToFilesMaps();
        recordDebugInfos = oldVersion.getRecordDebugInfos();
        readFiles = oldVersion.getReadFiles();
    }

    /** Constructor to replace the old AccessLogger with a new one, only knowing which attributes have to be replaced at runtime. */
    private AccessLogger(AccessLogger oldVersion, Map<Integer, RecordBase> records , Set<File> accessedFiles, Set<URL> accessedResources,
    Map<String, Map<Closeable, File>> newInputStreamsToFilesMaps,
    List<String> recordDebugInfos, Set<File> readFiles) {
        this(oldVersion);
        if (records != null) {
            this.records = records;
        }
        if (accessedFiles != null) {
            this.accessedFiles = accessedFiles;
        }
        if (accessedResources != null) {
            this.accessedResources = accessedResources;
        }
        if(newInputStreamsToFilesMaps != null) {
            for (Entry<String, Map<Closeable, File>> entry: newInputStreamsToFilesMaps.entrySet()) {
                this.inputStreamsToFilesMaps.put(entry.getKey(), entry.getValue());
            }
        }
        if (recordDebugInfos != null) {
            this.recordDebugInfos = recordDebugInfos;
        }
        if(readFiles != null) {
            this.readFiles = readFiles;
        }
    }


    /** Constructor to replace the old AccessLogger with a new one, only knowing which attributes have to be replaced at runtime. */
    private AccessLogger(AccessLogger oldVersion, Map<Integer, RecordBase> records , Set<File> accessedFiles, Set<URL> accessedResources,
    Map<String, Map<Closeable, File>> newInputStreamsToFilesMaps,
    List<String> recordDebugInfos) {
        this(oldVersion, records, accessedFiles, accessedResources, newInputStreamsToFilesMaps, recordDebugInfos, null);
    }

    public static void updateLogger(Map<Integer, RecordBase> records , Set<File> accessedFiles, Set<URL> accessedResources,
    Map<String, Map<Closeable, File>> newInputStreamsToFilesMaps, List<String> recordDebugInfos, Set<File> readFiles) {
        ACCESS_LOGGER = new AccessLogger(getLogger(), records , accessedFiles, accessedResources,newInputStreamsToFilesMaps, recordDebugInfos, readFiles);
    }

    public static void updateLogger(Map<Integer, RecordBase> records , Set<File> accessedFiles, Set<URL> accessedResources,
    Map<String, Map<Closeable, File>> newInputStreamsToFilesMaps, List<String> recordDebugInfos) {
        ACCESS_LOGGER = new AccessLogger(getLogger(), records , accessedFiles, accessedResources,newInputStreamsToFilesMaps, recordDebugInfos);
    }

    /** log that a FileInputStream was created with the file that provides its input */
    public static int logFileInputStreamCreated(FileInputStream fileInputStream, File file) {
        InputStreamCreatedRecord record = new InputStreamCreatedRecord(fileInputStream, file);
        //FileInputStreamCreatedFileRecord record = new FileInputStreamCreatedFileRecord(fileInputStream, file);
        getLogger().appendRecord(record);
        return record.recordId;
    }

    public static int logFileImageInputStreamCreated(ImageInputStream fileImageInputStream, File file) {
        ImageInputStreamCreatedFileRecord record = new ImageInputStreamCreatedFileRecord(fileImageInputStream, file);
        getLogger().appendRecord(record);
        return record.recordId;
    }

    /** log an acquired resource */
    public static int logResourceAcquired(URL resourceURL) {
        ResourceAcquiredRecord record = new ResourceAcquiredRecord(resourceURL);
        getLogger().appendRecord(record);
        return record.recordId;
    }

    public static int logInputStreamCreatedWithPath(InputStream inputStream, Path path) {
        InputStreamCreatedRecord record = new InputStreamCreatedRecord(inputStream, path.toFile());
        getLogger().appendRecord(record);
        return record.recordId;
    }

    public static int logInputStreamRead(InputStream inputStream) {
        InputStreamReadRecord record = new InputStreamReadRecord(inputStream);
        getLogger().appendRecord(record);
        return record.recordId;
    }

    /** log the stacktrace to a given recordId */
    public static int logStackTrace(int recordId, StackTraceElement[] stackTrace) {
        StackTraceRecord record = new StackTraceRecord(recordId, stackTrace);
        getLogger().appendRecord(record);
        return record.recordId;
    }

    // compute the accessed files and resources from the records
    public static void fillAccessedLists() {

        synchronized(getLogger()) { 
            Collection<RecordBase> tmp = new ArrayList<>(getLogger().records.values());

            for (RecordBase recordObj: tmp) {
                // I don't really like this, this implementation with "side effects" due to missing return values...
                // but for now it is there to enforce the singleton principle
                if (recordObj != null) {
                    ((RecordBase)recordObj).updateLists(getLogger());
                }
            }
        }
    }

    public void appendRecord(RecordBase record) {
        synchronized(this) {
            this.records.put(record.recordId, record);
        }
    }

    /** the only method that should be used to get the AccessLogger to ensure a singleton */
    public static AccessLogger getLogger() {
        if (ACCESS_LOGGER == null) {
            ACCESS_LOGGER = new AccessLogger();
        }

        return ACCESS_LOGGER;
    }

}
