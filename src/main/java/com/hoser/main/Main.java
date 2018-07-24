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
import java.util.concurrent.CountDownLatch;

public class Main {

    private static Logger logger = LogManager.getRootLogger();
    private static final String SAMPLE = "SampleVideo_1280x720_1mb.mp4";

    private static CountDownLatch playingLatch = new CountDownLatch(1);
    private static CountDownLatch snapshotTakenLatch = new CountDownLatch(1);


    public static void main(final String[] args) {

        String vlcPath = getResourcePath("vlc");
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcPath);
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

        Extractor extractor = new Extractor(playingLatch, snapshotTakenLatch);
        String sampleVideoPath = getResourcePath("videos/" + SAMPLE);

        if(extractor.getMediaPlayer().startMedia(sampleVideoPath)){

            try {
                playingLatch.await();
                logger.debug("The playing latch is: {}", playingLatch.getCount());

                extractor.getMediaPlayer().pause();
                snapshotTakenLatch.await();
                extractor.getMediaPlayer().stop();
                extractor.getMediaPlayer().release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getResourcePath(String resourceName){
        String resourcePath = null;
        ClassLoader classLoader = HeadlessMediaPlayer.class.getClassLoader();

        try{
            File resourceFile = new File(classLoader.getResource(resourceName).getFile());
            resourcePath = resourceFile.getAbsolutePath();
            logger.debug("The resource path is: {}", resourcePath);
        }catch (NullPointerException e){
            logger.error("Resource could not be found: {}", resourceName);
            System.exit(-1);
        }
        return resourcePath;
    }
}
