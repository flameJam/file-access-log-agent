package com.file_access_agent.logger;

import java.io.FileInputStream;

public abstract class FileInputStreamCreatedRecordBase extends FileInputStreamRecordBase {
        public FileInputStreamCreatedRecordBase(FileInputStream fileInputStream) {
            super(fileInputStream);
        }
}