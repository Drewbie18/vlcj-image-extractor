package com.hoser.simple.extractor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

public class Extractor {

    private static Logger logger = LogManager.getRootLogger();

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

    public Extractor() {
        configureEventsListener();
    }

    public final CountDownLatch snapshotTakenLatch = new CountDownLatch(1);

    private MediaPlayerFactory factory = new MediaPlayerFactory(VLC_ARGS);
    private MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    private void configureEventsListener(){
        mediaPlayer.addMediaPlayerEventListener(
                new MediaPlayerEventAdapter() {

                    @Override
                    public void playing(MediaPlayer mediaPlayer) {
                        logger.debug("The media is playing");
                    }
                    @Override
                    public void finished(MediaPlayer mediaPlayer) {
                        logger.debug("The media has finished");
                    }
                    @Override
                    public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
                        logger.debug("The length is: {} ", newLength);
                        mediaPlayer.saveSnapshot(getImageFile("3"));

                        try {
                            snapshotTakenLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                        logger.debug("Snapshot taken at: {}", filename);
                        snapshotTakenLatch.countDown();
                    }
                }
        );}

    private File getImageFile(String append){
        return Paths.get("./extractor-output", "image-" + append + ".png")
                .toFile();
    }

    public void takeSnapshot(String number){
        mediaPlayer.saveSnapshot(getImageFile(number));
    }
}
