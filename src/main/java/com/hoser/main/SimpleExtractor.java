package com.hoser.main;

import com.hoser.image.extractor.utils.FileUtils;
import com.hoser.image.extractor.utils.LoadNativeVlc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleExtractor {

    private static Logger logger = LogManager.getRootLogger();

    private static CountDownLatch snapShotLatch;
    private static AtomicInteger imageCounter = new AtomicInteger(0);

    private static final int DEFAULT_NUM_IMAGES = 10;
    private static int numImages;
    private static File outputDir;

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

        if(args.length < 1){
            logger.error("Missing argument, required video name or video absolute path");
            System.exit(-1);
        }

        boolean vlcLoaded = LoadNativeVlc.initializeVlc();
        if(!vlcLoaded){
            logger.error("Native VLC files were not found, exiting");
            System.exit(-1);
        }

        String videoName = args[0];
        File videoFile = null;
        try {
            videoFile = FileUtils.getVideoPath(videoName);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }

        try {
            outputDir = FileUtils.createDirectory(videoFile.getName());
        } catch (IOException e) {
            logger.error("Failed to create image output directory: {}",
                    videoName);
            logger.error(e.getMessage());
            System.exit(-1);
        }

        numImages = getNumImages(args);

        snapShotLatch = new CountDownLatch(numImages);
        logger.info("Number of images: {}", numImages);

        MediaPlayerFactory factory = new MediaPlayerFactory(VLC_ARGS);
        MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();

        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                logger.info("media finished");
                int count = imageCounter.get();
                if(count <= numImages){
                    mediaPlayer.play();
                }

            }

            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
                float percentPosition = newPosition * 100;
                logger.info("position changed: {}%", percentPosition);

                String imageAppend = String.valueOf(imageCounter.get());
                File imageFile = FileUtils.getImageFile(outputDir, imageAppend);
                mediaPlayer.saveSnapshot(imageFile);
            }

            @Override
            public void playing(MediaPlayer mediaPlayer) {
                logger.info("The media is playing.");
                int count = imageCounter.get();
                if(count <= numImages){
                    logger.debug("setting position from play event listener");
                    float position = count/(float)numImages; //set position to % of video
                    mediaPlayer.setPosition(position);
                }
            }

            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                logger.info("Snapshot was taken at: {}", filename);
                snapShotLatch.countDown();
                int count = imageCounter.incrementAndGet();
                if(count <= numImages){
                    float position = count/(float)numImages; //set position to % of video
                    mediaPlayer.setPosition(position);
                }
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
                logger.debug("The media has paused");
                mediaPlayer.stop();
            }
        });

        if (mediaPlayer.startMedia(videoFile.getAbsolutePath())) {

            try {
                snapShotLatch.await();
                logger.info("Snapshot latch is: {}", snapShotLatch.getCount());
            } catch (InterruptedException e) { //fail as gracefully as possible
               logger.error("Thread: {}, was interrupted waiting for all snapshots," +
                               " this should not happen. Releasing resources.",
                       Thread.currentThread().getName() );
               logger.error(e.getMessage());
               Thread.currentThread().interrupt();
               mediaPlayer.release();
               factory.release();
            }
        }

        mediaPlayer.release();
        factory.release();
        System.exit(0);
    }


    private static int getNumImages(String [] args){
        int number = DEFAULT_NUM_IMAGES;
        if(args.length < 2){
            logger.info("Second argument wasn't given using default number of images: {}",
                    DEFAULT_NUM_IMAGES);
        }else{
            try{
                number = Integer.valueOf(args[1]);
            }catch (NumberFormatException e){
                logger.error("Image number argument was invalid: {}, using default {}",
                        args[1],
                        DEFAULT_NUM_IMAGES);
            }
        }
        return number;
    }

}
