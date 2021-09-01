package com.burlywoodpalette.bwvideo.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "external-services")
public class ExternalServiceConfig {

  private BWPaletteServiceConfig bwpalette;

  @Getter
  @Setter
  public static class BWPaletteServiceConfig {

    private String url;
  }

}
