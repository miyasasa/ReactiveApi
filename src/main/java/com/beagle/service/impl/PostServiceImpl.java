package com.beagle.service.impl;

import com.beagle.api.ApiConfiguration;
import com.beagle.domain.Post;
import com.beagle.service.PostService;
import com.beagle.util.UriUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

  private static Logger LOG = LoggerFactory.getLogger(PostServiceImpl.class);

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ApiConfiguration configuration;

  @Override
  public Post addPost(Post post) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    ObjectMapper mapper = new ObjectMapper();
    String valueAsString = null;
    try {
      valueAsString = mapper.writeValueAsString(post);

    } catch (JsonProcessingException e) {
      e.printStackTrace();
      LOG.info("Mapper error: from object to json as String");
    }

    HttpEntity<Object> entity = new HttpEntity<>(valueAsString, headers);

    ResponseEntity<Post> exchange = restTemplate.exchange(configuration.getPostsPath(), HttpMethod.POST, entity, Post.class);

    return exchange.getBody();
  }

  @Override
  public void deletePost(String id) {
    restTemplate.exchange(UriUtil.getUri(configuration.getPostsPath(), id), HttpMethod.DELETE, null, Void.class);
  }

  @Override
  public Post getPost(String id) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Object> entity = new HttpEntity<>(headers);

    ResponseEntity<Post> exchange = restTemplate.exchange(UriUtil.getUri(configuration.getPostsPath(), id), HttpMethod.GET, entity, Post.class);

    return exchange.getBody();
  }

  @Override
  public List<Post> getAllPost() {

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(configuration.getPostsPath());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<Post[]> exchange = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, httpEntity, Post[].class);

    return new ArrayList<>(Arrays.asList(exchange.getBody()));
  }
}
