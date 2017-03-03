package com.beagle.controller;

import com.beagle.Application;
import com.beagle.domain.Post;
import com.beagle.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.LinkedList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
public class PostControllerTest {

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @MockBean
  private PostService postService;

  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).dispatchOptions(true).build();
  }

  @Test
  public void test_add_post() throws Exception {

    Post post = getPost();
    ObjectMapper mapper = new ObjectMapper();
    String postString = mapper.writeValueAsString(post);

    Mockito.when(postService.addPost(Mockito.anyObject())).thenReturn(post);

    mockMvc.perform(post("/post").contentType(MediaType.APPLICATION_JSON).content(postString))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId", is(post.getUserId())))
        .andExpect(jsonPath("$.id", is(post.getId())))
        .andExpect(jsonPath("$.title", is(post.getTitle())))
        .andExpect(jsonPath("$.body", is(post.getBody())));
  }

  @Test
  public void test_delete_post() throws Exception {
    Mockito.doNothing().when(postService);
    mockMvc.perform(delete("/post/{id}", "testId")).andExpect(status().isOk()).andExpect(jsonPath("$", is("removed")));
  }

  @Test
  public void test_get_post() throws Exception {
    Post post = getPost();
    Mockito.when(postService.getPost(Mockito.anyString())).thenReturn(post);
    mockMvc.perform(get("/post/{id}", post.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId", is(post.getUserId())))
        .andExpect(jsonPath("$.id", is(post.getId())))
        .andExpect(jsonPath("$.title", is(post.getTitle())))
        .andExpect(jsonPath("$.body", is(post.getBody())));
  }

  @Test
  public void test_get_all_post() throws Exception {
    Mockito.when(postService.getAllPost()).thenReturn(new LinkedList<>(Arrays.asList(getPost(), getPost(), getPost())));
    mockMvc.perform(get("/post")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)));
  }

  public Post getPost() {
    Post post = new Post();
    post.setId("testId");
    post.setTitle("testTitle");
    post.setBody("testBody");
    post.setUserId("testUserId");

    return post;
  }

}