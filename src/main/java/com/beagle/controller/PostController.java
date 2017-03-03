package com.beagle.controller;

import com.beagle.domain.Post;
import com.beagle.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/post")
public class PostController {

  @Autowired
  private PostService postService;

  @RequestMapping(method = RequestMethod.POST)
  @ResponseBody
  public Post addPost(@RequestBody Post post) throws JsonProcessingException {
    return postService.addPost(post);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ResponseBody
  public String deletePost(@PathVariable("id") String id) {
    postService.deletePost(id);
    return "removed";
  }

  @RequestMapping(value = "/{id}")
  @ResponseBody
  public Post getPost(@PathVariable("id") String id) {
    return postService.getPost(id);
  }

  @RequestMapping(method = RequestMethod.GET)
  @ResponseBody
  public List<Post> getAllPost() {
    return postService.getAllPost();
  }

}
