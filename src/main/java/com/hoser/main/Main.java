package com.hoser.main;

import com.hoser.simple.extractor.Extractor;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.io.File;

public class Main {

    private static Logger logger = LogManager.getRootLogger();
    static final String SAMPLE = "SampleVideo_1280x720_1mb.mp4";

    public static void main(final String[] args) {

        String vlcPath = getResourcePath("vlc");
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcPath);
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

        Extractor extractor = new Extractor();
        String sampleVideoPath = getResourcePath("videos/" + SAMPLE);

        extractor.getMediaPlayer().startMedia(sampleVideoPath);

    }

    private static String getResourcePath(String resourceName){
        String resourcePath = null;
        ClassLoader classLoader = HeadlessMediaPlayer.class.getClassLoader();

        try{
            File resourceFile = new File(classLoader.getResource(resourceName).getFile());
            resourcePath = resourceFile.getAbsolutePath();
            logger.debug("The resource path is: {}", resourcePath);
        }catch (NullPointerException e){
            logger.error("VLC player resource could not be found");
            System.exit(-1);
        }
        return resourcePath;
    }
}
