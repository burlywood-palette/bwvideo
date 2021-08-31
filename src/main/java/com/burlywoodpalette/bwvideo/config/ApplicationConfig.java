package com.burlywoodpalette.bwvideo.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "bwvideo-config")
public class ApplicationConfig {

  private Property property;

  @Getter
  @Setter
  public static class Property {

    private int imageCountResult;
  }
}
