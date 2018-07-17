package com.hoser.main;

import com.hoser.simple.player.SimplePlayer;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import java.io.File;

public class Main {

    static final String SAMPLE = "SampleVideo_1280x720_1mb.mp4";

    public static void main(final String[] args) {
        File file = new File("image-extractor/src/main/resources/vlc");
        String vlcPath = file.getAbsolutePath();
        System.out.println(file.exists());
        System.out.println(vlcPath);

        System.out.println(RuntimeUtil.getLibVlcLibraryName());

        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcPath);
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);


        File videoFile = new File("image-extractor/src/main/resources/videos/" + SAMPLE);


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimplePlayer(videoFile.getAbsolutePath());
            }
        });
    }
}
