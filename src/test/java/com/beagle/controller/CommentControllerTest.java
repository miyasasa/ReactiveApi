package com.beagle.controller;

import com.beagle.Application;
import com.beagle.domain.Comment;
import com.beagle.service.CommentService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
public class CommentControllerTest {

  @MockBean
  private CommentService commentService;

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).dispatchOptions(true).build();
  }

  @Test
  public void test_get_all_comment() throws Exception {

    Comment comment = getComment();

    Mockito.when(commentService.getAllComment()).thenReturn(new ArrayList<>(Arrays.asList(comment, getComment(), getComment())));

    mockMvc.perform(get("/comments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].postId", is(comment.getPostId())))
        .andExpect(jsonPath("$[0].id", is(comment.getId())))
        .andExpect(jsonPath("$[0].name", is(comment.getName())))
        .andExpect(jsonPath("$[0].email", is(comment.getEmail())))
        .andExpect(jsonPath("$[0].body", is(comment.getBody())));


  }

  @Test
  public void test_get_post_comments() throws Exception {

    Mockito.when(commentService.getPostComments(Mockito.anyString())).thenReturn(new ArrayList<>(Arrays.asList(getComment(), getComment(), getComment())));

    mockMvc.perform(get("/post/{id}/comment", "testPostId"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)));
  }

  @Test
  public void test_get_post_comments_via_param() throws Exception {

    Mockito.when(commentService.getPostCommentsViaParam(Mockito.anyString())).thenReturn(new ArrayList<>(Arrays.asList(getComment(), getComment(), getComment())));

    mockMvc.perform(get("/comment").param("postId", "testPostId"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)));
  }

  public Comment getComment() {
    Comment comment = new Comment();
    comment.setId("testId");
    comment.setPostId("testPostId");
    comment.setName("testName");
    comment.setEmail("test@gmail.com");
    comment.setBody("testBody");

    return comment;
  }
}