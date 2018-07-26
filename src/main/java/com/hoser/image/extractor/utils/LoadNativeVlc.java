package com.hoser.image.extractor.utils;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p>
 * Helper class to find LibVlc native libraries. This will first use vlcj's
 * built in native discovery method.
 * <p>
 * <p>
 * If that fails this will then try to use this app's default location which should be
 * the CWD/vlc.
 * </p>
 */
public class LoadNativeVlc {

    private static Logger logger = LogManager.getRootLogger();

    private LoadNativeVlc() {
    }

    public static boolean initializeVlc() {

        boolean foundWithDiscovery = new NativeDiscovery().discover();
        if (foundWithDiscovery) {
            logger.info("VLC libs found with native discovery");
            return true;
        }

        //use package vlc directory CWD/vlc
        String cWD = System.getProperty("user.dir");
        Path packageVlcPath = Paths.get(cWD, "vlc");

        if (packageVlcPath.toFile().exists()) {
            String vlc = packageVlcPath.toAbsolutePath().toString();
            logger.info("VLC library found at {}", vlc);
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlc);
            Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
            return true;
        }
        return false;
    }
}
