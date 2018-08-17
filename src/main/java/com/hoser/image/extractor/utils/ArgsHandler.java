package com.hoser.image.extractor.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>
 *     Simple class to handle the command line arguments given to extractor.
 * </p>
 */
public class ArgsHandler {
    private static Logger logger = LogManager.getRootLogger();
    private static final int DEFAULT_NUM_IMAGES = 10;
    private ArgsHandler(){

    }

    public static boolean checkInit(String [] args){
        return  (args.length == 0 && LoadNativeVlc.initializeVlc());
    }


    public static int getNumImages(String [] args){
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
