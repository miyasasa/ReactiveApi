package com.beagle.api;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = ApiProperties.class)
public class ApiConfiguration {

  private final ApiProperties properties;

  public ApiConfiguration(ApiProperties properties) {
    this.properties = properties;
  }

  public String getPostsPath() {
    return properties.getHost() + properties.getPostsPath();
  }

  public String getCommentsPath() {
    return properties.getHost() + properties.getCommentsPath();
  }

  public String getComments() {
    return properties.getCommentsPath();
  }
}
