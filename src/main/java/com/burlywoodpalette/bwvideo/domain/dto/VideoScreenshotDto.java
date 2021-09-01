package com.burlywoodpalette.bwvideo.domain.dto;

import java.awt.image.BufferedImage;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoScreenshotDto {

  private UUID videoId;
  private BufferedImage image;

}
