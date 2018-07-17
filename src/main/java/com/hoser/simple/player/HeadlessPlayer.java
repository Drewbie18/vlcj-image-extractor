package com.hoser.simple.player;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

//TODO look at ThumbsTest.java on caprica git for all
//args to make the player truly headless.
public class HeadlessPlayer {

    private HeadlessMediaPlayer player;
    private final String filePath;
    private final String V_OUT ="--vout";
    private final String DUMMY = "dummy";

    public HeadlessPlayer(String filePath) {
        this.filePath = filePath;
    }

    public void initPlayer(){
        MediaPlayerFactory factory = new MediaPlayerFactory(V_OUT, DUMMY);
        player = factory.newHeadlessMediaPlayer();
    }


}
