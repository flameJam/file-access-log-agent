package com.file_access_agent.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.file_access_agent.common.util.json.JsonUtil;

/** 
 * Class used during in instrumentation code to log the access to different resources, files,...
 * Can "keep state" and compute dependencies between different "log-entries"
 * but does all these computations only when told to compute & generate the outputfile
 * in order to impair the instrumented target's performance as little as possible.
 */
public class AccessLogger {

    /** the used AccessLogger Instance, used to implement a Singleton */
    private static AccessLogger ACCESS_LOGGER;

    /** the List of Records recorded/logged during testing*/
    private Map<Integer, RecordBase> records;

    /** the list of accessed files */
    private Set<File> accessedFiles;

    /** the list of access resources -> URLs */
    private Set<URL> accessedResources;

    /** a list to map FileInputStreams to Files to log when a file was read (currently unused) */
    private Map<FileInputStream, File> fileInputStreamMap;

    private List<String> recordDebugInfos;

    public List<String> getRecordDebugInfos() {
        return recordDebugInfos;
    }

    public Map<FileInputStream, File> getFileInputStreamMap() {
        return fileInputStreamMap;
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
        fileInputStreamMap = new HashMap<>();
        recordDebugInfos = new ArrayList<>();
    }

    private AccessLogger(AccessLogger oldVersion) {
        records = oldVersion.getRecords();
        accessedFiles = oldVersion.getAccessedFiles();
        accessedResources = oldVersion.getAccessedResources();
        fileInputStreamMap = oldVersion.getFileInputStreamMap();
        recordDebugInfos = oldVersion.getRecordDebugInfos();
    }

    /** Constructor to replace the old AccessLogger with a new one, only knowing which attributes have to be replaced at runtime. */
    private AccessLogger(AccessLogger oldVersion, Map<Integer, RecordBase> records , Set<File> accessedFiles, Set<URL> accessedResources,
    Map<FileInputStream, File> fileInputStreamMap, List<String> recordDebugInfos) {
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
        if (fileInputStreamMap != null) {
            this.fileInputStreamMap = fileInputStreamMap;
        }
        if (recordDebugInfos != null) {
            this.recordDebugInfos = recordDebugInfos;
        }
    }

    public static void updateLogger(Map<Integer, RecordBase> records , Set<File> accessedFiles, Set<URL> accessedResources,
    Map<FileInputStream, File> fileInputStreamMap, List<String> recordDebugInfos) {
        ACCESS_LOGGER = new AccessLogger(getLogger(), records , accessedFiles, accessedResources, fileInputStreamMap, recordDebugInfos);
    }

    /** log that a FileInputStream was created with the file that provides its input */
    public static int logFileInputStreamCreated(FileInputStream fileInputStream, File file) {
        FileInputStreamCreatedFileRecord record = new FileInputStreamCreatedFileRecord(fileInputStream, file);
        getLogger().appendRecord(record);
        return record.recordId;
    }

    /** log that a FileInputStream was created with the FileDescriptor was used to provide its input */
    public static int logFileInputStreamCreated(FileInputStream fileInputStream, FileDescriptor fileDescriptor) {
        FileInputStreamCreatedFileDescriptorRecord record = new FileInputStreamCreatedFileDescriptorRecord(fileInputStream, fileDescriptor);
        getLogger().appendRecord(record);
        return record.recordId;
    }

    /** log an acquired resource */
    public static int logResourceAcquired(URL resourceURL) {
        ResourceAcquiredRecord record = new ResourceAcquiredRecord(resourceURL);
        getLogger().appendRecord(record);
        return record.recordId;
    }

    /** log the stacktrace to a given recordId */
    public static int logStackTrace(int recordId, StackTraceElement[] stackTrace) {
        StackTraceRecord record = new StackTraceRecord(recordId, stackTrace);
        getLogger().appendRecord(record);
        return record.recordId;
    }

    /** compute the accessed resources & files and put them into the output file */
    public static void provideOutput() {
        fillAccessedLists();
        long testTimestamp = System.currentTimeMillis();
        getLogger().writeToOutputFile(getOutputFilePath(testTimestamp), testTimestamp);
    }

    /** compute the output file path from the file_access_agent.properties */
    private static String getOutputFilePath(long testTimestamp) {
        Properties agentProps = new Properties();
        try {
            agentProps.load(ClassLoader.getSystemResourceAsStream("file_access_agent.properties"));
        } catch (IOException e) {
            System.err.println("FileAccessAgent failed to get its properties!");
            e.printStackTrace();
            return null;
        }

        String outputFilePath = agentProps.getProperty("output_file_location");
        String outputFilePrefix = agentProps.getProperty("output_file_prefix");
        return outputFilePath + "/" + outputFilePrefix + testTimestamp + ".json";

    }

    /**  write this AccessLoggers contents to a file ath the given location */
    private void writeToOutputFile(String outputPath, long testTimestamp) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(outputPath, false);
        } catch (IOException e) {
            System.err.println("Error while trying to open FileWriter for agent output file.");
            e.printStackTrace();
            return;
        }

        BufferedWriter writer = new BufferedWriter(fileWriter);
        
        String content = JsonUtil.getOutputJsonString(getAccessedFiles(), getAccessedResources(), getRecordDebugInfos(), testTimestamp);
        
        try {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Error while trying to write to agent output file.");
            e.printStackTrace();
        }

        try {
            writer.close();
        } catch (IOException e) {
            System.err.println("Error while trying to close BufferedWriter to agent output file.");
            e.printStackTrace();
        }
    
    }

    // compute the accessed files and resources from the records
    private static void fillAccessedLists() {

        Collection<RecordBase> records = getLogger().records.values();
        for (RecordBase recordObj: records) {
            // I don't really like this, this implementation with "side effects" due to missing return values...
            // but for now it is there to enforce the singleton principle
            ((RecordBase)recordObj).updateLists(getLogger());
        }
    }

    public void appendRecord(RecordBase record) {
        this.records.put(record.recordId, record);
    }

    /** the only method that should be used to get the AccessLogger to ensure a singleton */
    private static AccessLogger getLogger() {
        if (ACCESS_LOGGER == null) {
            ACCESS_LOGGER = new AccessLogger();
        }

        return ACCESS_LOGGER;
    }

}
