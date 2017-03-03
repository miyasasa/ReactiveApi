package com.beagle.service;

import com.beagle.domain.Post;

import java.util.List;

public interface PostService {

  Post addPost(Post post);

  void deletePost(String id);

  Post getPost(String id);

  List<Post> getAllPost();
}
