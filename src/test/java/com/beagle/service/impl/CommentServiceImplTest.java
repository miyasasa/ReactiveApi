package com.beagle.service.impl;

import com.beagle.Application;
import com.beagle.BaseTest;
import com.beagle.domain.Comment;
import com.beagle.service.CommentService;

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
public class CommentServiceImplTest extends BaseTest {

  private static final String GET_ALL_COMMENT_PATH = "data/comment/all_comment.json";
  private static final String POST_COMMENT_PATH = "data/comment/post_comment.json";

  @Autowired
  private CommentService commentService;

  @Test
  public void test_get_post_comment() throws IOException, URISyntaxException {
    server.expect(anything()).andRespond(withSuccess(loadFile(POST_COMMENT_PATH), MediaType.APPLICATION_JSON));

    List<Comment> postComments = commentService.getPostComments("1");

    assertNotNull(postComments);
    assertEquals(5, postComments.size());
  }

  @Test
  public void test_get_post_comment_via_param() throws IOException, URISyntaxException {
    server.expect(anything()).andRespond(withSuccess(loadFile(POST_COMMENT_PATH), MediaType.APPLICATION_JSON));

    List<Comment> postComments = commentService.getPostCommentsViaParam("1");

    assertNotNull(postComments);
    assertEquals(5, postComments.size());
  }

  @Test
  public void test_get_all_comment() throws IOException, URISyntaxException {
    server.expect(anything()).andRespond(withSuccess(loadFile(GET_ALL_COMMENT_PATH), MediaType.APPLICATION_JSON));

    List<Comment> allComment = commentService.getAllComment();

    assertNotNull(allComment);
    assertEquals(500, allComment.size());
  }
}