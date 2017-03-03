package com.beagle.controller.rx;

import com.beagle.service.RxService;

import org.slf4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RxController {

  private static Logger LOG = org.slf4j.LoggerFactory.getLogger(RxController.class);

  private final RxService rxService;

  private final SimpMessagingTemplate brokerMessagingTemplate;

  public RxController(RxService rxService, SimpMessagingTemplate brokerMessagingTemplate) {
    this.rxService = rxService;
    this.brokerMessagingTemplate = brokerMessagingTemplate;
  }

  @RequestMapping(value = {"/rx/post", "/rx/comment", "/rx/mix", "/rx/post/comment"})
  public String getIndex() {
    return "index";
  }

  @MessageMapping(value = "/service/rx/post")
  public void getPostsViaRx() {
    rxService.getPostsViaRx();
  }

  @MessageMapping("/service/rx/comment")
  public void getCommentsViaRx() {
    rxService.getCommentsViaRx();
  }

  @MessageMapping(value = "/service/rx/mix")
  public void getPostAndCommentsBothViaRx() {
    rxService.getPostAndCommentMixedViaRx();
  }

  // get first 10 post's first three comment
  @MessageMapping(value = "/service/rx/post/comment")
  public void getPostCommentViaAjax() {
    rxService.getPostsCommentsViaRx();
  }
}
