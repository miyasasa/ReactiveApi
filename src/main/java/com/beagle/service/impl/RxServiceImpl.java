package com.beagle.service.impl;

import com.beagle.api.ApiConfiguration;
import com.beagle.domain.Comment;
import com.beagle.domain.Post;
import com.beagle.service.RxService;
import com.beagle.util.UriUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

@Service
public class RxServiceImpl implements RxService {

  private static Logger LOG = LoggerFactory.getLogger(RxServiceImpl.class);

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ApiConfiguration configuration;

  @Autowired
  private SimpMessagingTemplate brokerMessagingTemplate;

  @Override
  public void getPostsViaRx() {

    LOG.info(":: GET ALL POST REQUEST ");

    Observable.just(restTemplate.exchange(configuration.getPostsPath(), HttpMethod.GET, null, Post[].class))
        .filter(responseEntity -> responseEntity.getStatusCode().is2xxSuccessful())
        .flatMap(responseEntity -> Observable.fromArray(responseEntity.getBody()))
        .observeOn(Schedulers.io())
        .doOnNext(post -> {
          Thread.sleep(100);
          LOG.info("POST : {} {}", post.getId(), post.getTitle());
          brokerMessagingTemplate.convertAndSend("/rx/post", post);
        }).subscribeOn(Schedulers.io()).subscribe();
  }

  @Override
  public void getCommentsViaRx() {

    LOG.info(":: GET 100 COMMENT REQUEST ");

    Observable.just(restTemplate.exchange(configuration.getCommentsPath(), HttpMethod.GET, null, Comment[].class))
        .filter(responseEntity -> responseEntity.getStatusCode().is2xxSuccessful())
        .flatMap(responseEntity -> Observable.fromArray(responseEntity.getBody()))
        .take(100)
        .observeOn(Schedulers.io())
        .doOnNext(comment -> {
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          LOG.info("COMMENT : {} {}", comment.getId(), comment.getName());
          this.brokerMessagingTemplate.convertAndSend("/rx/comment", comment);
        }).subscribeOn(Schedulers.io()).subscribe();
  }

  @Override
  public void getPostAndCommentMixedViaRx() {

    LOG.info(":: GET POST(10) AND COMMENT(10) MIX REQUEST ");

    Observable<Post> posts = Observable.just(restTemplate.exchange(configuration.getPostsPath(), HttpMethod.GET, null, Post[].class))
        .filter(responseEntity -> responseEntity.getStatusCode().is2xxSuccessful())
        .flatMap(responseEntity -> Observable.fromArray(responseEntity.getBody()))
        .observeOn(Schedulers.io())
        .doOnNext(post -> {
          brokerMessagingTemplate.convertAndSend("/rx/post", post);
          LOG.info("POST : {} {} ", post.getId(), post.getTitle());
        })
        .subscribeOn(Schedulers.io())
        .take(10);

    Observable<Comment> comments = Observable.just(restTemplate.exchange(configuration.getCommentsPath(), HttpMethod.GET, null, Comment[].class))
        .filter(responseEntity -> responseEntity.getStatusCode().is2xxSuccessful())
        .flatMap(responseEntity -> Observable.fromArray(responseEntity.getBody()))
        .observeOn(Schedulers.io())
        .doOnNext(comment -> {
          brokerMessagingTemplate.convertAndSend("/rx/comment", comment);
          LOG.info("COMMENT : {} {} ", comment.getId(), comment.getName());
        })
        .subscribeOn(Schedulers.io())
        .take(10);

    Observable.zip(posts, comments, (post, comment) -> {
      Thread.sleep(100);
      return post;
    }).subscribeOn(Schedulers.io()).subscribe();
  }

  @Override
  public void getPostsCommentsViaRx() {

    LOG.info(":: GET FIRST (10)POST's  (3)COMMENT REQUEST ");

    Observable.just(restTemplate.exchange(configuration.getPostsPath(), HttpMethod.GET, null, Post[].class))
        .filter(responseEntity -> responseEntity.getStatusCode().is2xxSuccessful())
        .flatMap(responseEntity -> Observable.fromArray(responseEntity.getBody()))
        .take(10)
        .observeOn(Schedulers.io())
        .doOnNext(post -> Observable.just(restTemplate.exchange(UriUtil.getUri(configuration.getPostsPath(), post.getId(), configuration.getComments()), HttpMethod.GET, null, Comment[].class))
            .filter(responseEntity -> responseEntity.getStatusCode().is2xxSuccessful())
            .flatMap(responseEntity -> Observable.fromArray(responseEntity.getBody()))
            .take(3)
            .observeOn(Schedulers.io())
            .doOnNext(comment -> {
              Thread.sleep(100);
              LOG.info("COMMENT : {} , {} {}", comment.getPostId(), comment.getId(), comment.getName());
              brokerMessagingTemplate.convertAndSend("/rx/comment", comment);
            }).subscribe()
        ).subscribeOn(Schedulers.io()).subscribe();
  }
}
