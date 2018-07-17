package com.hoser.util.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;


public class BasicTests {

    Logger logger = LogManager.getRootLogger();


    @Test
    public void logTest(){

        String var = "A variable!";
        logger.info("INFO - {}", var);
        logger.debug("DEBUG");
        logger.warn("WARN");
        logger.error("ERROR");


    }

}
