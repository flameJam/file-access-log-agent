package com.file_access_agent.logger;

import java.io.FileInputStream;

/** abstract Record storing a FileInputStream that was created */
public abstract class FileInputStreamCreatedRecordBase extends FileInputStreamRecordBase {
        public FileInputStreamCreatedRecordBase(FileInputStream fileInputStream) {
            super(fileInputStream);
        }
}