package com.hoser.image.extractor.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileUtils {

    private static Logger logger = LogManager.getRootLogger();

    private FileUtils() {
    }

    public static File createDirectory(String videoName) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String folderName = videoName + "-" +timeStamp;
        String cwd = System.getProperty("user.dir");
        Path path = Paths.get(cwd, "extractor-output", folderName);
        logger.debug(path.toString());

        if(!path.toFile().exists()){
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                logger.error("Failed to create image output directory: {}", path);
                logger.error(e.getMessage());
            }
        }
        return path.toFile();
    }

}
