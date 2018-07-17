package com.hoser.filter.test;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class FilterTests {

    private Logger logger = LogManager.getRootLogger();

    static final String SAMPLE = "SampleVideo_1280x720_1mb.mp4";

    @Test
    public void playMedia(){

        File file = new File("src/main/resources/vlc");
        String vlcPath = file.getAbsolutePath();
        System.out.println(file.exists());
        System.out.println(vlcPath);

        System.out.println(RuntimeUtil.getLibVlcLibraryName());

        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcPath);
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

       EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        JFrame frame = new JFrame("A MEDIA PLAYER");
        frame.setBounds(100,100,600,400);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(true);

        File videoFile = new File("src/main/resources/videos/" + SAMPLE);
        if(videoFile.isFile()){
            logger.debug("File found now trying to play");
            mediaPlayerComponent.getMediaPlayer().playMedia(videoFile.getAbsolutePath());
        }

    }

}
