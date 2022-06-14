package com.file_access_agent.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AccessLogger {

    private static AccessLogger ACCESS_LOGGER;


    private List<RecordBase> records;
    private Set<File> accessedFiles;
    private Set<URL> accessedResources;
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
 
    public AccessLogger() {
        records = new ArrayList<>();
        accessedFiles = new HashSet<>();
        accessedResources = new HashSet<>();
        fileInputStreamMap = new HashMap<>();
    }

    public AccessLogger(AccessLogger oldVersion) {
        records = oldVersion.getRecords();
        accessedFiles = oldVersion.getAccessedFiles();
        accessedResources = oldVersion.getAccessedResources();
        fileInputStreamMap = oldVersion.getFileInputStreamMap();
    }

    public AccessLogger(AccessLogger oldVersion, List<RecordBase> records , Set<File> accessedFiles, Set<URL> accessedResources,
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

    public static void logFileInputStreamCreated(FileInputStream fileInputStream, File file) {
        getLogger().appendRecord(new FileInputStreamCreatedFileRecord(fileInputStream, file));
    }

    public static void logFileInputStreamCreated(FileInputStream fileInputStream, FileDescriptor fileDescriptor) {
        getLogger().appendRecord(new FileInputStreamCreatedFileDescriptorRecord(fileInputStream, fileDescriptor));
    }

    public static void logResourceAcquired(URL resourceURL) {
        getLogger().appendRecord(new ResourceAcquiredRecord(resourceURL));
    }

    public static void provideOutput(String outputPath) {
        fillAccessedLists();
        getLogger().writeToOutputFile(outputPath);
    }

    private void writeToOutputFile(String outputPath) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(outputPath, false);
        } catch (IOException e) {
            System.err.println("Error while trying to open FileWriter for agent output file.");
            e.printStackTrace();
            return;
        }

        BufferedWriter writer = new BufferedWriter(fileWriter);
        
        String content = "Accessed Files:\n";
        for (File file: this.getAccessedFiles()) {
            content += file.getAbsolutePath() + "\n";
        }
        content += "Accessed Resources:\n";
        for (URL resourceURL: this.getAccessedResources()) {
                content += getPathForResourceURL(resourceURL) + "\n";
            
        }
        
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

    private static String getPathForResourceURL(URL resourceURL) {
        URI resourceURI;
        try {
            resourceURI = resourceURL.toURI();
        } catch (URISyntaxException uriSyntaxExc) {
            printStacktraceWithIgnoreWarning(uriSyntaxExc);
            return String.format("URL %s could not be resolved to an URI", resourceURL.toString());
        }
        Path resourcePath = null;
        try {
            resourcePath = Path.of(resourceURI);
        } catch (IllegalArgumentException illArgExc) {
            printStacktraceWithIgnoreWarning(illArgExc);
            return String.format("URI %s could not be resolved to a path - IllegalArgument", resourceURI);
        } catch (FileSystemNotFoundException fileSysNotFoundExc) {
            printStacktraceWithIgnoreWarning(fileSysNotFoundExc);
            return String.format("URI %s could not be resolved to a path - FileSystemNotFound", resourceURI);
        } catch (SecurityException securityException) {
            printStacktraceWithIgnoreWarning(securityException);
            return String.format("URI %s could not be resolved to a path - SecurityException", resourceURI);
        }

        try {
            resourcePath = resourcePath.toAbsolutePath();
        } catch (SecurityException securityException) {
            printStacktraceWithIgnoreWarning(securityException);
            return String.format("Path %s could not be resolved to an absolute path - SecurityException", resourcePath);
        } catch (IOError ioError) {
            printStacktraceWithIgnoreWarning(ioError);
            return String.format("Path %s could not be resolved to an absolute path", resourcePath.toString());
        }
        return resourcePath.toString();
    }

    private static void printStacktraceWithIgnoreWarning(Throwable throwable) {
        System.err.println("The following exception/error occured during file-access-log-agent output computation and was ignored:");
        throwable.printStackTrace();
    }

    private static void fillAccessedLists() {

        List<RecordBase> records = getLogger().records;
        for (RecordBase record: records) {
           ACCESS_LOGGER = record.updateLists(getLogger());
        }
    }

    public void appendRecord(RecordBase record) {
        this.records.add(record);
    }

    private static AccessLogger getLogger() {
        if (ACCESS_LOGGER == null) {
            ACCESS_LOGGER = new AccessLogger();
        }

        return ACCESS_LOGGER;
    }

}
