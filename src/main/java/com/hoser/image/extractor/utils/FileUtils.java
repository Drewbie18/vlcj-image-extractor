package com.hoser.image.extractor.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p>
 * Utilities to create a folder for a video's extracted images.
 * As well as determining if a string argument is a file or directory.
 * </p>
 */
public class FileUtils {
    private static Logger logger = LogManager.getRootLogger();

    private FileUtils() {
    }

    /**
     * <p>
     * Will create a directory to store the extracted images in the <b>CWD/extractor-images</b> folder.
     * It will use the video name and a timestamp to make the directory name unique.
     * </p>
     *
     * @param videoName Name of the video that is to have images extracted.
     * @return a file representation of the new directory.
     */
    public static File createDirectory(String videoName) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String folderName = videoName + "-" + timeStamp;
        String cwd = System.getProperty("user.dir");
        Path path = Paths.get(cwd, "extractor-output", folderName);
        logger.debug(path.toString());

        if (!path.toFile().exists()) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                logger.error("Failed to create image output directory: {}", path);
                logger.error(e.getMessage());
            }
        }
        return path.toFile();
    }

    /**
     * <p>
     * Determines if the input string passed to the method is absolute or
     * relative and ensures that it can be accessed.
     * </p>
     *
     * @param inputSting argument passed to main method.
     * @return the absolute path of the existing file
     * @throws FileNotFoundException will be thrown if the input is a file
     * that can't be found <b>or</b> is a directory
     */
    public static String getVideoPath(final String inputSting) throws FileNotFoundException {

        String ret;
        String error = String.format("Input: %s was a file that was not" +
                        " found or is a directory",
                inputSting);
        Path path = Paths.get(inputSting);

        if (path.isAbsolute()) {
            logger.info("Path is absolute");
            File file = path.toFile();

            if (!file.isFile()) {
                throw new FileNotFoundException(error);
            }
            ret = file.getAbsolutePath();
        } else { //see if the file is in the CWD
            logger.info("Path is not relative might just be a filename");
            String cWd = System.getProperty("user.dir");
            File file = Paths.get(cWd, inputSting).toFile();

            if (!file.isFile()) {
                throw new FileNotFoundException(error);
            }
            ret = file.getAbsolutePath();
        }
        logger.info("File found at: {}", ret);
        return ret;
    }

}
