package com.beagle;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.Resource;

import static com.beagle.Constants.COMMENT_REQUEST;
import static com.beagle.Constants.POST_REQUEST;
import static com.beagle.Constants.RX_GET_10_COMMENT_PATH;
import static com.beagle.Constants.RX_GET_10_POST_PATH;
import static com.beagle.Constants.RX_GET_COMMENT_PATH;
import static com.beagle.Constants.RX_GET_POST_PATH;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public abstract class BaseTest {

  @Resource
  private RestTemplate restTemplate;

  protected MockRestServiceServer server;

  @Before
  public void setUp() throws Exception {
    server = MockRestServiceServer.bindTo(restTemplate).build();
  }

  protected byte[] loadFile(String resourcePath) throws URISyntaxException, IOException {
    ClassLoader loader = getClass().getClassLoader();

    URL resource = loader.getResource(resourcePath);

    assertNotNull(resource);

    return IOUtils.toByteArray(Files.newInputStream(Paths.get(resource.toURI())));
  }

  protected final void mockServerForPostsRequest() throws IOException, URISyntaxException {
    server.expect(requestTo(POST_REQUEST)).andRespond(withSuccess(loadFile(RX_GET_POST_PATH), MediaType.APPLICATION_JSON));

  }

  protected final void mockServerForCommentsRequest() throws IOException, URISyntaxException {
    server.expect(requestTo(COMMENT_REQUEST)).andRespond(withSuccess(loadFile(RX_GET_COMMENT_PATH), MediaType.APPLICATION_JSON));

  }

  protected final void mockServerForPostsAndCommentsMixRequest() throws IOException, URISyntaxException {
    server.expect(requestTo(POST_REQUEST)).andRespond(withSuccess(loadFile(RX_GET_10_POST_PATH), MediaType.APPLICATION_JSON));
    server.expect(requestTo(COMMENT_REQUEST)).andRespond(withSuccess(loadFile(RX_GET_10_COMMENT_PATH), MediaType.APPLICATION_JSON));
  }

  protected final void mockServerForPostsCommentRequest() throws IOException, URISyntaxException {

    server.expect(requestTo("https://jsonplaceholder.typicode.com/posts/")).andRespond(withSuccess(loadFile(RX_GET_10_POST_PATH), MediaType.APPLICATION_JSON));

    server.expect(requestTo("https://jsonplaceholder.typicode.com/posts/1_test/comments/")).andRespond(withSuccess(loadFile(getPath("1_test")), MediaType
        .APPLICATION_JSON));
    server.expect(requestTo("https://jsonplaceholder.typicode.com/posts/2/comments/")).andRespond(withSuccess(loadFile(getPath("2")), MediaType
        .APPLICATION_JSON));
    server.expect(requestTo("https://jsonplaceholder.typicode.com/posts/3/comments/")).andRespond(withSuccess(loadFile(getPath("3")), MediaType
        .APPLICATION_JSON));
    server.expect(requestTo("https://jsonplaceholder.typicode.com/posts/4/comments/")).andRespond(withSuccess(loadFile(getPath("4")), MediaType
        .APPLICATION_JSON));
    server.expect(requestTo("https://jsonplaceholder.typicode.com/posts/5/comments/")).andRespond(withSuccess(loadFile(getPath("5")), MediaType
        .APPLICATION_JSON));
    server.expect(requestTo("https://jsonplaceholder.typicode.com/posts/6/comments/")).andRespond(withSuccess(loadFile(getPath("6")), MediaType
        .APPLICATION_JSON));
    server.expect(requestTo("https://jsonplaceholder.typicode.com/posts/7/comments/")).andRespond(withSuccess(loadFile(getPath("7")), MediaType
        .APPLICATION_JSON));
    server.expect(requestTo("https://jsonplaceholder.typicode.com/posts/8/comments/")).andRespond(withSuccess(loadFile(getPath("8")), MediaType
        .APPLICATION_JSON));
    server.expect(requestTo("https://jsonplaceholder.typicode.com/posts/9/comments/")).andRespond(withSuccess(loadFile(getPath("9")), MediaType
        .APPLICATION_JSON));
    server.expect(requestTo("https://jsonplaceholder.typicode.com/posts/10/comments/")).andRespond(withSuccess(loadFile(getPath("10")), MediaType
        .APPLICATION_JSON));

  }

  public final String getPath(String num) {
    return "data/rx/post_comments/rx_posts_" + num + "_comments.json";
  }

}
