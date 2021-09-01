package com.burlywoodpalette.bwvideo.domain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

@Data
@Builder
public class PersistFileDto {

  private Mono<FilePart> fileParts;
  private String title;
  private String contentType;

}
