package com.burlywoodpalette.bwvideo.service;

import com.burlywoodpalette.bwvideo.domain.dto.VideoScreenshotDto;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Flux;

public interface ImageProcessor {

  Flux<VideoScreenshotDto> process(UUID videoId);

}
