package com.beagle.service.impl;

import com.beagle.api.ApiConfiguration;
import com.beagle.domain.Comment;
import com.beagle.service.CommentService;
import com.beagle.util.UriUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ApiConfiguration configuration;

  @Override
  public List<Comment> getAllComment() {

    Comment[] comments = restTemplate.getForObject(configuration.getCommentsPath(), Comment[].class);

    return new ArrayList<>(Arrays.asList(comments));
  }

  @Override
  public List<Comment> getPostComments(String postId) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Object> entity = new HttpEntity<>(headers);

    ResponseEntity<Comment[]> exchange = restTemplate.exchange(UriUtil.getUri(configuration.getPostsPath(), postId, configuration.getComments()), HttpMethod.GET, entity,
        Comment[].class);

    return new ArrayList<>(Arrays.asList(exchange.getBody()));
  }

  @Override
  public List<Comment> getPostCommentsViaParam(String postId) {

    MultiValueMap params = new LinkedMultiValueMap<>();
    params.add("postId", postId);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(configuration.getCommentsPath());
    builder.queryParams(params);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Object> entity = new HttpEntity<>(headers);


    ResponseEntity<Comment[]> exchange = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, Comment[].class);

    return new ArrayList<>(Arrays.asList(exchange.getBody()));
  }
}
