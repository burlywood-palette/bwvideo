package com.burlywoodpalette.bwvideo.service;

import com.burlywoodpalette.bwvideo.domain.dto.PersistFileDto;
import com.burlywoodpalette.bwvideo.domain.dto.VideoScreenshotDto;
import com.burlywoodpalette.bwvideo.service.file.FileStorageService;
import java.awt.image.BufferedImage;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DefaultImageProcessor implements ImageProcessor {

  private final VideoProcessor<BufferedImage> videoProcessor;
  private final FileStorageService<PersistFileDto> fileStorageService;

  @Autowired
  public DefaultImageProcessor(VideoProcessor<BufferedImage> videoProcessor,
      FileStorageService<PersistFileDto> fileStorageService) {
    this.videoProcessor = videoProcessor;
    this.fileStorageService = fileStorageService;
  }

  @Override
  public Flux<VideoScreenshotDto> process(UUID videoId) {
    return fileStorageService.getResource(videoId.toString())
        .flatMap(ReactiveGridFsResource::getInputStream)
        .map(videoProcessor::process)
        .flatMapMany(Flux::fromIterable)
        .map(bf ->
            VideoScreenshotDto.builder()
                .image(bf)
                .videoId(videoId)
                .build());
  }
}
