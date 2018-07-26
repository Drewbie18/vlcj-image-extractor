package com.hoser.image.extractor.utils.test;

import com.hoser.image.extractor.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static junit.framework.TestCase.assertTrue;

public class UtilsTest {
    private static Logger logger = LogManager.getRootLogger();

    @Test
    public void createDirectoryTest(){
        File file = FileUtils.createDirectory("mr.bear");
        assertTrue(file.exists());
        boolean deleted = false;
        try {
            deleted  =  Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
           logger.error(e.getMessage());
        }
        assertTrue(deleted);
    }

}
