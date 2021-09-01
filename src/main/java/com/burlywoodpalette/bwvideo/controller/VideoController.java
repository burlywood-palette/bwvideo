package com.burlywoodpalette.bwvideo.controller;

import com.burlywoodpalette.bwvideo.domain.dto.PersistFileDto;
import com.burlywoodpalette.bwvideo.service.file.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

  private final FileStorageService<PersistFileDto> fileStorageService;

  public VideoController(
      FileStorageService<PersistFileDto> fileStorageService) {
    this.fileStorageService = fileStorageService;
  }

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Mono<String> uploadVideo(@RequestPart("file") Mono<FilePart> uploadedFile) {
    return uploadedFile.map(item -> PersistFileDto.builder()
            .fileParts(uploadedFile)
            .title(item.filename())
            .build())
        .flatMap(fileStorageService::save)
        .doOnError(error -> log.error("Exception in upload video", error));
  }
}
