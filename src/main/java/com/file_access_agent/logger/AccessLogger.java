package com.file_access_agent.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.file_access_agent.util.json.JsonUtil;

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
    private List<RecordBase> records;

    /** the list of accessed files */
    private Set<File> accessedFiles;

    /** the list of access resources -> URLs */
    private Set<URL> accessedResources;

    /** a list to map FileInputStreams to Files to log when a file was read (currently unused) */
    private Map<FileInputStream, File> fileInputStreamMap;

    
    public Map<FileInputStream, File> getFileInputStreamMap() {
        return fileInputStreamMap;
    }

    public List<RecordBase> getRecords() {
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
        records = new ArrayList<>();
        accessedFiles = new HashSet<>();
        accessedResources = new HashSet<>();
        fileInputStreamMap = new HashMap<>();
    }

    private AccessLogger(AccessLogger oldVersion) {
        records = oldVersion.getRecords();
        accessedFiles = oldVersion.getAccessedFiles();
        accessedResources = oldVersion.getAccessedResources();
        fileInputStreamMap = oldVersion.getFileInputStreamMap();
    }

    /** Constructor to replace the old AccessLogger with a new one, only knowing which attributes have to be replaced at runtime. */
    private AccessLogger(AccessLogger oldVersion, List<RecordBase> records , Set<File> accessedFiles, Set<URL> accessedResources,
    Map<FileInputStream, File> fileInputStreamMap) {
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
    }

    public static void updateLogger(List<RecordBase> records , Set<File> accessedFiles, Set<URL> accessedResources,
    Map<FileInputStream, File> fileInputStreamMap) {
        ACCESS_LOGGER = new AccessLogger(getLogger(), records , accessedFiles, accessedResources, fileInputStreamMap);
    }

    /** log that a FileInputStream was created with the file that provides its input */
    public static void logFileInputStreamCreated(FileInputStream fileInputStream, File file) {
        getLogger().appendRecord(new FileInputStreamCreatedFileRecord(fileInputStream, file));
    }

    /** log that a FileInputStream was created with the FileDescriptor was used to provide its input */
    public static void logFileInputStreamCreated(FileInputStream fileInputStream, FileDescriptor fileDescriptor) {
        getLogger().appendRecord(new FileInputStreamCreatedFileDescriptorRecord(fileInputStream, fileDescriptor));
    }

    /** log an acquired resource */
    public static void logResourceAcquired(URL resourceURL) {
        getLogger().appendRecord(new ResourceAcquiredRecord(resourceURL));
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
        
        String content = JsonUtil.getOutputJsonString(getAccessedFiles(), getAccessedResources(), testTimestamp);
        
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

        List<RecordBase> records = getLogger().records;
        for (RecordBase record: records) {
            // I don't really like this, this implementation with "side effects" due to missing return values...
            // but for now it is there to enforce the singleton principle
           record.updateLists(getLogger());
        }
    }

    public void appendRecord(RecordBase record) {
        this.records.add(record);
    }

    /** the only method that should be used to get the AccessLogger to ensure a singleton */
    private static AccessLogger getLogger() {
        if (ACCESS_LOGGER == null) {
            ACCESS_LOGGER = new AccessLogger();
        }

        return ACCESS_LOGGER;
    }

}
