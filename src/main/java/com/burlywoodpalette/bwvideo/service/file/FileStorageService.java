package com.burlywoodpalette.bwvideo.service.file;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsResource;
import reactor.core.publisher.Mono;

public interface FileStorageService<T> {

  Mono<String> save(T value);

  Mono<Void> remove(String id);

  Mono<ReactiveGridFsResource> getResource(String id);
}
