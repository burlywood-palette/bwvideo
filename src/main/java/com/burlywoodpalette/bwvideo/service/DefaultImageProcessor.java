package com.burlywoodpalette.bwvideo.service;

import com.burlywoodpalette.bwvideo.config.ApplicationConfig;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DefaultImageProcessor implements ImageProcessor {

  private final Random random;
  private final ApplicationConfig applicationConfig;

  @Autowired
  public DefaultImageProcessor(ApplicationConfig applicationConfig) {
    this.applicationConfig = applicationConfig;
    this.random = new Random();
  }

  @Override
  public List<BufferedImage> process(InputStream inputStream) {
    inputStream = Optional.ofNullable(inputStream)
        .orElseThrow(() -> new IllegalArgumentException("InputStream video is null"));
    List<BufferedImage> bufferedImages = new ArrayList<>();
    try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
      FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputStream);
      grabber.start();
      int lengthInVideoFrames = grabber.getLengthInVideoFrames();

      IntStream.range(0, applicationConfig.getProperty().getImageCountResult())
          .map(i -> getRandomFrame(lengthInVideoFrames))
          .forEach(frame -> {
            try {
              grabber.setVideoFrameNumber(frame);
              bufferedImages.add(deepCopy(converter.convert(grabber.grabImage())));
            } catch (Exception e) {
              log.error("Error process screenshot from video", e);
            }
          });
      grabber.stop();
    } catch (IOException e) {
      log.error("Something wrong with work grabber", e);
    }
    return bufferedImages;
  }

  private int getRandomFrame(int lengthInVideoFrames) {
    return random.nextInt(lengthInVideoFrames);
  }

  private BufferedImage deepCopy(BufferedImage bi) {
    ColorModel cm = bi.getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    WritableRaster raster = bi.copyData(null);
    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
  }
}
