package com.beagle.service.impl;


import com.beagle.Application;
import com.beagle.BaseTest;
import com.beagle.domain.Post;
import com.beagle.service.PostService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
public class PostServiceImplTest extends BaseTest {

  private static final String GET_ALL_POST_PATH = "data/post/all_post.json";
  private static final String GET_POST_PATH = "data/post/post.json";
  private static final String ADD_POST_PATH = "data/post/add_post.json";

  @Autowired
  private PostService postService;

  @Test
  public void test_add_post() throws IOException, URISyntaxException {

    server.expect(anything()).andRespond(withSuccess(loadFile(ADD_POST_PATH), MediaType.APPLICATION_JSON));

    Post post = new Post();
    post.setId("1");
    post.setUserId("1");
    post.setTitle("sunt aut facere repellat provident occaecati excepturi optio reprehenderit");
    post.setBody("quia et suscipit suscipit recusandae consequuntur expedita et cum reprehenderit molestiae ut ut quas totam nostrum rerum est autem sunt rem" +
        " eveniet architecto");

    Post responsePost = postService.getPost("1");

    assertNotNull(post);
    assertEquals(post.getId(), responsePost.getId());
    assertEquals(post.getUserId(), responsePost.getUserId());
    assertEquals(post.getTitle(), responsePost.getTitle());
    assertEquals(post.getBody(), responsePost.getBody());
  }

  @Test
  public void test_remove_post() throws IOException, URISyntaxException {
    server.expect(anything()).andRespond(withSuccess(loadFile(GET_POST_PATH), MediaType.APPLICATION_JSON));
    postService.deletePost("1");
  }

  @Test
  public void test_get_post() throws IOException, URISyntaxException {
    server.expect(anything()).andRespond(withSuccess(loadFile(GET_POST_PATH), MediaType.APPLICATION_JSON));

    Post post = postService.getPost("1");

    assertNotNull(post);
    assertEquals("1", post.getId());
    assertEquals("1", post.getUserId());
    assertEquals("sunt aut facere repellat provident occaecati excepturi optio reprehenderit", post.getTitle());
    assertEquals("quia et suscipit suscipit recusandae consequuntur expedita et cum reprehenderit molestiae ut ut quas totam nostrum rerum est autem sunt rem" +
        " eveniet architecto", post.getBody());
  }

  @Test
  public void test_get_all_post() throws IOException, URISyntaxException {
    server.expect(anything()).andRespond(withSuccess(loadFile(GET_ALL_POST_PATH), MediaType.APPLICATION_JSON));

    List<Post> allPost = postService.getAllPost();

    assertNotNull(allPost);
    assertEquals(100, allPost.size());
  }
}