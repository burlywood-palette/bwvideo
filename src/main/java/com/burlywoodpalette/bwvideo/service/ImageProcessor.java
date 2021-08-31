package com.burlywoodpalette.bwvideo.service;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

public interface ImageProcessor {

  List<BufferedImage> process(InputStream inputStream);

}
