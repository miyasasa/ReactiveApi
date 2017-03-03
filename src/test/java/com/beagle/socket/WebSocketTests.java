package com.beagle.socket;

import com.beagle.BaseTest;
import com.beagle.domain.Comment;
import com.beagle.domain.Post;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class WebSocketTests extends BaseTest {

  private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

  private SockJsClient sockJsClient;

  private WebSocketStompClient stompClient;

  @LocalServerPort
  private int port;

  @Before
  public void setup() {
    this.sockJsClient = new SockJsClient(new ArrayList<>(Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()))));
    this.stompClient = new WebSocketStompClient(sockJsClient);
    this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
  }

  @After
  public void tearDown() throws Exception {
    this.stompClient.stop();
  }

  @Test
  public void test_get_posts() throws Exception {

    mockServerForPostsRequest();

    List<Post> posts = new LinkedList<>();
    CountDownLatch lock = new CountDownLatch(100);

    StompSessionHandler handler = new TestSessionHandler(new AtomicReference<>()) {

      @Override
      public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {

        session.subscribe("/rx/post", new StompFrameHandler() {

          @Override
          public Type getPayloadType(StompHeaders headers) {
            return Post.class;
          }

          @Override
          public void handleFrame(StompHeaders headers, Object payload) {
            Post post = (Post) payload;
            posts.add(post);
            lock.countDown();
          }

        });

        session.send("/app/service/rx/post", "get posts".getBytes());
      }
    };

    this.stompClient.connect("ws://localhost:" + this.port + "/gs-guide-websocket", this.headers, handler);

    lock.await();

    assertNotNull(posts);
    assertEquals(100, posts.size());
  }

  @Test
  public void test_get_comments() throws Exception {

    mockServerForCommentsRequest();

    List<Comment> comments = new LinkedList<>();
    CountDownLatch lock = new CountDownLatch(100);

    StompSessionHandler handler = new TestSessionHandler(new AtomicReference<>()) {

      @Override
      public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {

        session.subscribe("/rx/comment", new StompFrameHandler() {

          @Override
          public Type getPayloadType(StompHeaders headers) {
            return Comment.class;
          }

          @Override
          public void handleFrame(StompHeaders headers, Object payload) {
            Comment comment = (Comment) payload;
            comments.add(comment);
            lock.countDown();
          }

        });

        session.send("/app/service/rx/comment", "get comments".getBytes());
      }
    };

    this.stompClient.connect("ws://localhost:" + this.port + "/gs-guide-websocket", this.headers, handler);

    lock.await();

    assertNotNull(comments);
    assertEquals(100, comments.size());
  }

  @Test
  public void test_get_post_and_comment_mix() throws Exception {

    mockServerForPostsAndCommentsMixRequest();

    List mixList = new LinkedList<>();
    CountDownLatch lock = new CountDownLatch(20);

    StompSessionHandler handler = new TestSessionHandler(new AtomicReference<>()) {

      @Override
      public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {

        session.subscribe("/rx/post", new StompFrameHandler() {
          @Override
          public Type getPayloadType(StompHeaders headers) {
            return Post.class;
          }

          @Override
          public void handleFrame(StompHeaders headers, Object payload) {
            Post post = (Post) payload;
            mixList.add(post);
            lock.countDown();
          }

        });

        session.subscribe("/rx/comment", new StompFrameHandler() {
          @Override
          public Type getPayloadType(StompHeaders stompHeaders) {
            return Comment.class;
          }

          @Override
          public void handleFrame(StompHeaders stompHeaders, Object payload) {
            Comment comment = (Comment) payload;
            mixList.add(comment);
            lock.countDown();
          }
        });

        session.send("/app/service/rx/mix", "get posts and comments mix".getBytes());
      }
    };

    this.stompClient.connect("ws://localhost:" + this.port + "/gs-guide-websocket", this.headers, handler);

    lock.await();

    assertNotNull(mixList);
    assertEquals(20, mixList.size());
  }

  @Test
  public void test_get__posts_comments() throws Exception {

    mockServerForPostsCommentRequest();

    List<Comment> comments = new LinkedList<>();
    CountDownLatch lock = new CountDownLatch(30);

    StompSessionHandler handler = new TestSessionHandler(new AtomicReference<>()) {

      @Override
      public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {

        session.subscribe("/rx/comment", new StompFrameHandler() {

          @Override
          public Type getPayloadType(StompHeaders headers) {
            return Comment.class;
          }

          @Override
          public void handleFrame(StompHeaders headers, Object payload) {
            Comment comment = (Comment) payload;
            comments.add(comment);
            lock.countDown();
          }

        });

        session.send("/app/service/rx/post/comment", "get comments".getBytes());
      }
    };

    this.stompClient.connect("ws://localhost:" + this.port + "/gs-guide-websocket", this.headers, handler);

    lock.await();

    assertNotNull(comments);
    assertEquals(30, comments.size());
  }

  private class TestSessionHandler extends StompSessionHandlerAdapter {

    private final AtomicReference<Throwable> failure;

    public TestSessionHandler(AtomicReference<Throwable> failure) {
      this.failure = failure;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
      this.failure.set(new Exception(headers.toString()));
    }

    @Override
    public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
      this.failure.set(ex);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable ex) {
      this.failure.set(ex);
    }
  }
}
