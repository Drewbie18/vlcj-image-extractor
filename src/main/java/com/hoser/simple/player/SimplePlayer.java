package com.hoser.simple.player;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Paths;

public class SimplePlayer {
    private static Logger logger = LogManager.getRootLogger();

    private final JFrame frame;

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public SimplePlayer(String filePath) {
        frame = new JFrame("My First Media Player");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(true);

        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(
                new MediaPlayerEventAdapter() {

                    @Override
                    public void playing(MediaPlayer mediaPlayer) {
                        logger.debug("The media is playing");
                        super.playing(mediaPlayer);
                        frame.setTitle("A FUCKING MEDIA PLAYER");
                    }

                    @Override
                    public void finished(MediaPlayer mediaPlayer) {
                        super.finished(mediaPlayer);
                        logger.debug("The media has finished");
                        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    }

                    @Override
                    public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
                        super.lengthChanged(mediaPlayer, newLength);
                        logger.debug("The length is: " + newLength);
                    }

                    @Override
                    public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                        super.snapshotTaken(mediaPlayer, filename);
                        logger.debug("Snapshot taken at: " + filename);
                        //  mediaPlayer.pause();
                        //skip(mediaPlayer);
                    }

                    @Override
                    public void opening(MediaPlayer mediaPlayer) {
                        super.opening(mediaPlayer);
                        logger.debug("Opened media file: {}", mediaPlayer.getMediaDetails().toString());
                    }

                }
        );

        MediaPlayer player = mediaPlayerComponent.getMediaPlayer();
        player.playMedia(filePath);
        logger.debug("passed play command!");

        //TODO use events and Java concurrent tools to replace this!
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        player.saveSnapshot(getImageFile("1"));

    }

    private void skip(MediaPlayer player) {
        player.skip(2000);
        logger.debug("Skipped 2");
        player.pause();
        File imageFile = new File("C:\\workspaces\\hoser\\images\\image-2.png");
        player.saveSnapshot(imageFile);
        player.play();
    }

    private File getImageFile(String append) {
        return Paths.get("./output", "image-" + append + ".png")
                .toFile();
    }


}
