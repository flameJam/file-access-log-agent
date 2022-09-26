package com.file_access_agent.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Set;

import com.file_access_agent.common.util.environment.OutputFileVar;
import com.file_access_agent.common.util.environment.RealReadVar;
import com.file_access_agent.common.util.json.JsonUtil;
import com.file_access_agent.logger.AccessLogger;

public class OutputProvider {

    /** compute the accessed resources & files and put them into the output file */
    public static void provideOutput() {
        AccessLogger.fillAccessedLists();
        long testTimestamp = System.currentTimeMillis();
        writeToOutputFile(getOutputFilePath(testTimestamp), testTimestamp, AccessLogger.getLogger());
    }
    
    /**  write this AccessLoggers contents to a file ath the given location */
    private static void writeToOutputFile(String outputPath, long testTimestamp, AccessLogger accessLogger) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(outputPath, false);
        } catch (IOException e) {
            System.err.println("Error while trying to open FileWriter for agent output file.");
            e.printStackTrace();
            return;
        }

        BufferedWriter writer = new BufferedWriter(fileWriter);
        Set<File> filesToLogAccessed;
        if (RealReadVar.isDefined() && RealReadVar.isInRealReadMode()) {
            filesToLogAccessed = accessLogger.getReadFiles();
        } else {
            filesToLogAccessed = accessLogger.getAccessedFiles();
        }

        String content = JsonUtil.getOutputJsonString(filesToLogAccessed, accessLogger.getAccessedResources(), accessLogger.getRecordDebugInfos(), testTimestamp);
        
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

    /** compute the output file path from the file_access_agent.properties */
    private static String getOutputFilePath(long testTimestamp) {

        Date dateCorrespondingTestTimestamp = new Date(testTimestamp);
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd_HH-mm-ss");

        if (OutputFileVar.isDefined()) {
            return OutputFileVar.getOutputPath() + "/" + OutputFileVar.getPrefix() + formatter.format(dateCorrespondingTestTimestamp) + ".json";
        }

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

}
