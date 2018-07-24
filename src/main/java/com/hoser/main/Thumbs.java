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

public class Thumbs {

    private static Logger logger = LogManager.getRootLogger();
    private static final String SAMPLE = "SampleVideo_1280x720_1mb.mp4";
    private static CountDownLatch snapshotTakenLatch = new CountDownLatch(1);
    private static CountDownLatch positionChangedLatch;
    private static CountDownLatch pausedLatch = new CountDownLatch(1);

    private static CountDownLatch firstPassLatch = new CountDownLatch(1);

    private static AtomicInteger positionCounter = new AtomicInteger(0);



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

        String sampleVideoPath = getResourcePath("videos/" + SAMPLE);

        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {


            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
                logger.debug("position changed: {}", newPosition);
                String fileAppender = String.valueOf(newPosition * 10);
                mediaPlayer.saveSnapshot(getImageFile(fileAppender));
            }

            @Override
            public void playing(MediaPlayer mediaPlayer) {
                logger.debug("The media is playing.");
            }

            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                logger.debug("Snapshot was taken at: {}", filename);
                firstPassLatch.countDown();
                positionChangedLatch = new CountDownLatch(1);
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
                logger.debug("The media has paused");
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                logger.debug("The media has finished");
            }
        });

        if (mediaPlayer.startMedia(sampleVideoPath)) {


            int position = 0;
            while(position <= 10){

                float positionSetter = (float) position/10;
                logger.debug("position setter: {}", positionSetter);
                mediaPlayer.setPosition(positionSetter);
                try {
                    firstPassLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    positionChangedLatch.await();
                    logger.debug("position changed latch value: {}",
                            positionChangedLatch.getCount());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                position = positionCounter.incrementAndGet();
            }
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
