# Image Extractor Using VLCJ

This is a utility that will 'extract' images from a video file. This leverages the [VLCJ](https://github.com/caprica/vlcj) Java Framework to load a video file and snapshot images of the video. 

## Prerequisites
- Java Version 8
- VLC Version 3.0+
    - Must be proper version for machine architecture. (32, 64)
    - VLC must either be installed in default location or you need have a copy of the VLC binary distribution handy.  

## Setup
- Download the appropriate distribution from [releases.](https://github.com/Drewbie18/vlcj-image-extractor/releases)

- Unpack the archive

- If you have VLC installed in the default location then VLCJ's native discovery should detect and be able to load the native libraries it requires. 

- If you have a copy of the VLC binaries and wish to use that, image-extractor defaults to looking in the the following location `ROOT/bin/vlc`. Drop the binaries for VLC directly into that location. 

## Running The Tool
- From `ROOT/bin` execute the following command:  `image-extractor.bat <VIDEO NAME> <NUMBER oF IMAGES>`
- <b>VIDEO NAME</b>: Either the name of a video you have copied into `ROOT/bin`<b> OR</b> the absolute path of a video on your filesystem. 
- <b>NUMBER OF IMAGES</b>: Optional argument, if none is supplied the default is 10. 
- The images will be placed in: `ROOT/bin/extractor-output/VIDEO_NAME-TIMESTAMP`

## Example Usage

A sample video file is included in the binary distribution. Browse to: `ROOT/bin`. From the command line execute: `image-extractor.bat sample_1.mp4`. Check the output directory to see the results. 

## Methodology
The extractor will snapshot the video at equal intervals, the size of the intervals is set by the number of images asked for. i.e: 10 images = (approx) one image every 10% of the video.  

## TODO
- Support running the extractor on an entire directory of videos. 
- Investigate/fix premature finishing of video and hanging for image quantities over 90

