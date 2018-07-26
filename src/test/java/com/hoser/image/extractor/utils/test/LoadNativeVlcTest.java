package com.hoser.image.extractor.utils.test;

import com.hoser.image.extractor.utils.LoadNativeVlc;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class LoadNativeVlcTest {

    @Test
    public void loadDefault(){
        assertTrue("load native VLC returned false!",LoadNativeVlc.initializeVlc());
    }
}
