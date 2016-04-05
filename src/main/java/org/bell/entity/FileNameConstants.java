package org.bell.entity;

import java.io.File;

public class FileNameConstants {
    public final static String MP3_FILE_NAME = new File(System.getProperty("user.dir"), "Bell.mp3").getAbsolutePath();
    public final static String JSON_FILE_NAME = System.getProperty("user.dir") + "/Bell.json";
    public final static String DB_NAME = "jdbc:h2:file:" + System.getProperty("user.dir") + "/BellDb;FILE_LOCK=FS;PAGE_SIZE=1024;CACHE_SIZE=8192";
}

