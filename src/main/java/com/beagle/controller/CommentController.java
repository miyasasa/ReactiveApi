package com.beagle.controller;

import com.beagle.domain.Comment;
import com.beagle.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CommentController {

  @Autowired
  private CommentService commentService;

  @RequestMapping(value = "/comments")
  @ResponseBody
  public List<Comment> getAllComment() {
    return commentService.getAllComment();
  }

  @RequestMapping(value = "/post/{id}/comment")
  @ResponseBody
  public List<Comment> getPostComments(@PathVariable("id") String id) {
    return commentService.getPostComments(id);
  }

  @RequestMapping(value = "/comment")
  @ResponseBody
  public List<Comment> getPostCommentViaParam(@RequestParam(value = "postId") String id) {
    return commentService.getPostCommentsViaParam(id);
  }

}
