package com.burlywoodpalette.bwvideo.service;

import java.io.InputStream;
import java.util.List;

public interface VideoProcessor<T> {

  List<T> process(InputStream inputStream);

}
