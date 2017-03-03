package com.beagle.api;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(ignoreUnknownFields = false, prefix = "api")
public class ApiProperties {

  @NotNull
  private String host;

  @NotNull
  private String postsPath;

  @NotNull
  private String commentsPath;

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getPostsPath() {
    return postsPath;
  }

  public void setPostsPath(String postsPath) {
    this.postsPath = postsPath;
  }

  public String getCommentsPath() {
    return commentsPath;
  }

  public void setCommentsPath(String commentsPath) {
    this.commentsPath = commentsPath;
  }
}
