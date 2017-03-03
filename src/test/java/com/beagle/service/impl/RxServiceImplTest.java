package com.beagle.service.impl;

import com.beagle.Application;
import com.beagle.BaseTest;
import com.beagle.service.RxService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
public class RxServiceImplTest extends BaseTest {

  @Autowired
  private RxService service;

  private TestMessageChannel messageChannel;

  private CountDownLatch latch;

  private SimpMessagingTemplate messagingTemplate;

  @Before
  public void setup() {
    this.messageChannel = new TestMessageChannel();
    this.messagingTemplate = new SimpMessagingTemplate(this.messageChannel);

    ReflectionTestUtils.setField(service, "brokerMessagingTemplate", messagingTemplate);
  }

  @Test
  public void test_get_posts() throws IOException, URISyntaxException, InterruptedException {

    mockServerForPostsRequest();

    latch = new CountDownLatch(100);

    service.getPostsViaRx();

    latch.await();

    List<Message<byte[]>> messages = this.messageChannel.getMessages();
    assertEquals(100, messages.size());
  }

  @Test
  public void test_get_comments() throws IOException, URISyntaxException, InterruptedException {

    mockServerForCommentsRequest();

    latch = new CountDownLatch(100);

    service.getCommentsViaRx();

    latch.await();

    List<Message<byte[]>> messages = this.messageChannel.getMessages();
    assertEquals(100, messages.size());
  }

  @Test
  public void test_get_posts_and_comments() throws IOException, URISyntaxException, InterruptedException {

    mockServerForPostsAndCommentsMixRequest();

    latch = new CountDownLatch(20);

    service.getPostAndCommentMixedViaRx();

    latch.await();

    List<Message<byte[]>> messages = this.messageChannel.getMessages();

    assertNotNull(messages);
    assertEquals(20, messages.size());
  }

  @Test
  public void test_get_posts_comment() throws IOException, URISyntaxException, InterruptedException {

    mockServerForPostsCommentRequest();

    latch = new CountDownLatch(30);

    service.getPostsCommentsViaRx();

    List<Message<byte[]>> messages = this.messageChannel.getMessages();

    this.latch.await();

    assertNotNull(messages);
  }

  public class TestMessageChannel implements SubscribableChannel {

    private final List<Message<byte[]>> messages = Collections.synchronizedList(new LinkedList<>());

    private final List<MessageHandler> handlers = new ArrayList<>();

    public List<Message<byte[]>> getMessages() {
      return this.messages;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean send(Message<?> message) {
      messages.add((Message<byte[]>) message);
      latch.countDown();

      return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean send(Message<?> message, long timeout) {
      this.messages.add((Message<byte[]>) message);
      return true;
    }

    @Override
    public boolean subscribe(MessageHandler handler) {
      this.handlers.add(handler);
      return true;
    }

    @Override
    public boolean unsubscribe(MessageHandler handler) {
      this.handlers.remove(handler);
      return true;
    }
  }

}