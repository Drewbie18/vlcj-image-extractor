package com.hoser.simple.player;

import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

//TODO look at ThumbsTest.java on caprica git for all
//args to make the player truly headless.
public class HeadlessPlayer {

    private HeadlessMediaPlayer player;
    private final String filePath;

    public HeadlessPlayer(String filePath) {
        this.filePath = filePath;
    }

}
