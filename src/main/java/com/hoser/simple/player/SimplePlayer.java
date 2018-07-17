package com.hoser.simple.player;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.File;

public class SimplePlayer {

    private final JFrame frame;

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public SimplePlayer(String filePath) {
        frame = new JFrame("My First Media Player");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(true);

        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(
                new MediaPlayerEventAdapter() {

                    @Override
                    public void playing(MediaPlayer mediaPlayer) {
                        System.out.println("The media is playing");
                        super.playing(mediaPlayer);
                        frame.setTitle("A FUCKING MEDIA PLAYER");
                    }

                    @Override
                    public void finished(MediaPlayer mediaPlayer) {
                        super.finished(mediaPlayer);
                        System.out.println("The media has finished");
                        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    }

                    @Override
                    public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
                        super.lengthChanged(mediaPlayer, newLength);
                        System.out.println("The length is: " + newLength);
                    }

                    @Override
                    public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                        super.snapshotTaken(mediaPlayer, filename);
                        System.out.println("Snapshot taken at: " + filename);
                        mediaPlayer.pause();
                        skip(mediaPlayer);

                    }

                }
        );

        MediaPlayer player = mediaPlayerComponent.getMediaPlayer();
        player.playMedia(filePath);
        player.skip(500);
        player.pause();
        File imageFile = new File("C:\\workspaces\\hoser\\images\\image-3.png");
        player.saveSnapshot(imageFile);
        System.out.println("passed play command!");

        //TODO use events and Java concurrent tools to replace this!
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void skip(MediaPlayer player) {
        player.skip(2000);
        System.out.println("Skipped 2");
        player.pause();
        File imageFile = new File("C:\\workspaces\\hoser\\images\\image-2.png");
        player.saveSnapshot(imageFile);
        player.play();
    }

}
