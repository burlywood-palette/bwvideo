package com.burlywoodpalette.bwvideo.service.file;

import static com.burlywoodpalette.bwvideo.domain.ErrorConstant.FILE_NOT_FOUND_ERROR_MESSAGE;

import com.burlywoodpalette.bwvideo.domain.dto.PersistFileDto;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsResource;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DefaultFileStorageService implements FileStorageService<PersistFileDto> {

  private final ReactiveGridFsTemplate reactiveGridFsTemplate;

  @Autowired
  public DefaultFileStorageService(ReactiveGridFsTemplate reactiveGridFsTemplate) {
    this.reactiveGridFsTemplate = reactiveGridFsTemplate;
  }

  @Override
  public Mono<String> save(PersistFileDto value) {
    return Mono.justOrEmpty(value)
        .flatMap(PersistFileDto::getFileParts)
        .flatMap(filePart -> reactiveGridFsTemplate.store(filePart.content(), value.getTitle(), value.getContentType()))
        .map(ObjectId::toString)
        .doOnNext(uid -> log.info("The {} has been stored successfully. mongoId: {}", value.getTitle(), uid))
        .doOnError(throwable -> log.error("{} {}", DefaultFileStorageService.class, throwable.getMessage()));
  }

  @Override
  public Mono<Void> remove(String id) {
    return Mono.justOrEmpty(id)
        .map(this::getQuery)
        .flatMap(reactiveGridFsTemplate::delete)
        .doOnNext(item -> log.info("Success deleted file by id: {}", id))
        .then();
  }

  @Override
  public Mono<ReactiveGridFsResource> getResource(String id) {
    return Mono.justOrEmpty(id)
        .map(this::getQuery)
        .flatMap(reactiveGridFsTemplate::findOne)
        .flatMap(reactiveGridFsTemplate::getResource)
        .switchIfEmpty(Mono.error(new IllegalStateException(FILE_NOT_FOUND_ERROR_MESSAGE)))
        .doOnError(error -> log.error(error.getMessage(), error));
  }

  private Query getQuery(String id) {
    return Query.query(Criteria.where("_id").is(id));
  }
}
