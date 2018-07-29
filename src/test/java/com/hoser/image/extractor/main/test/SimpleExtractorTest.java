package com.hoser.image.extractor.main.test;

import com.hoser.main.SimpleExtractor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

public class SimpleExtractorTest {

    private static final String SAMPLE = "sample-video/sample_1.mp4";
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();


    @Test
    public void singleExtractorTwoArgsTest(){
        exit.expectSystemExitWithStatus(0);
        String[] args ={SAMPLE, "15"};
        SimpleExtractor.main(args);
    }
}
