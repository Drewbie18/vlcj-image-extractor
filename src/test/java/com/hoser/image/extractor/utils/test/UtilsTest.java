package com.hoser.image.extractor.utils.test;

import com.hoser.image.extractor.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

public class UtilsTest {
    private static Logger logger = LogManager.getRootLogger();

    private static final String SAMPLE = "sample-video/SampleVideo_1280x720_1mb.mp4";

    @Test
    public void createDirectoryTest() {
        File file = null;
        try {
            file = FileUtils.createDirectory("mr.bear");
        } catch (IOException e) {
            logger.error(e.getMessage());
            fail("Directory was not created!");
        }
        assertNotNull(file);
        boolean deleted = false;
        try {
            deleted = Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        assertTrue(deleted);
    }

    @Test(expected = FileNotFoundException.class)
    public void fileNotExist() throws FileNotFoundException {

        try {
            FileUtils.getVideoPath("NOT_REAL");
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Test
    public void fileExist() {
        try {
            String absPath = FileUtils.getVideoPath(SAMPLE);
            assertTrue(absPath.contains("SampleVideo"));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }
    }

}
