package com.hoser.main;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ExtractorFlow {

    private static Logger logger = LogManager.getRootLogger();
    private static final String SHORT_SAMPLE = "SampleVideo_1280x720_1mb.mp4";
    private static final String LONG_SAMPLE = "SampleVideo_1280x720_5mb.mp4";

    private static CountDownLatch snapShotLatch = new CountDownLatch(99);
    private static AtomicInteger imageCounter = new AtomicInteger(0);

    private static final String[] VLC_ARGS = {
            "--intf", "dummy",          /* no interface */
            "--vout", "dummy",          /* we don't want video (output) */
            "--no-audio",               /* we don't want audio (decoding) */
            "--no-osd",
            "--no-spu",
            "--no-stats",               /* no stats */
            "--no-sub-autodetect-file", /* we don't want subtitles */
            //    "--no-inhibit",             /* we don't want interfaces */
            "--no-disable-screensaver", /* we don't want interfaces */
            "--no-snapshot-preview",    /* no blending in dummy vout */
    };

    public static void main(String[] args) {

        String vlcPath = getResourcePath("vlc");
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcPath);
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

        MediaPlayerFactory factory = new MediaPlayerFactory(VLC_ARGS);
        MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();

        String sampleVideoPath = getResourcePath("videos/" + LONG_SAMPLE);

        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                logger.debug("media finished");
            }

            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
                logger.debug("position changed: {}", newPosition);
                mediaPlayer.saveSnapshot(getImageFile(String.valueOf(imageCounter.get())));
            }

            @Override
            public void playing(MediaPlayer mediaPlayer) {
                logger.debug("The media is playing.");
            }

            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                logger.debug("Snapshot was taken at: {}", filename);
                snapShotLatch.countDown();
                if(imageCounter.incrementAndGet() < 100){
                    logger.debug("The counter is at {}", imageCounter.get());
                    float position = imageCounter.get()/100f;
                    mediaPlayer.setPosition(position);
                }
            }
            @Override
            public void paused(MediaPlayer mediaPlayer) {
                logger.debug("The media has paused");
            }
        });

        if (mediaPlayer.startMedia(sampleVideoPath)) {

            //set position
            mediaPlayer.setPosition(0.01f);
            try {
                snapShotLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            logger.debug("passed latched");
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        factory.release();
    }

    private static File getImageFile(String append) {
        return Paths.get("./extractor-output", "image-" + append + ".png")
                .toFile();
    }


    private static String getResourcePath(String resourceName) {
        String resourcePath = null;
        ClassLoader classLoader = HeadlessMediaPlayer.class.getClassLoader();

        try {
            File resourceFile = new File(classLoader.getResource(resourceName).getFile());
            resourcePath = resourceFile.getAbsolutePath();
            logger.debug("The resource path is: {}", resourcePath);
        } catch (NullPointerException e) {
            logger.error("Resource could not be found: {}", resourceName);
            System.exit(-1);
        }
        return resourcePath;
    }

}
