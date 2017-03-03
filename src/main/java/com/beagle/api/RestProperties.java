package com.beagle.api;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(ignoreUnknownFields = false, prefix = "rest.client")
public class RestProperties {

  private int readTimeout;

  private int connectionTimeout;

  public int getReadTimeout() {
    return readTimeout;
  }

  public void setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
  }

  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }
}
