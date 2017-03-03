package com.beagle.service;

import com.beagle.domain.Comment;

import java.util.List;

public interface CommentService {

  List<Comment> getAllComment();

  List<Comment> getPostComments(String postId);

  List<Comment> getPostCommentsViaParam(String postId);
}
