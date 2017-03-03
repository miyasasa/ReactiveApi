package com.beagle.controller.rx;

import com.beagle.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
public class RxControllerTest {

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  public void test_get_index_page_via_rx_post_request() throws Exception {
    mockMvc.perform(get("/rx/post")).andExpect(status().isOk()).andExpect(view().name("index"));
  }

  @Test
  public void test_get_index_page_via_rx_comment_request() throws Exception {
    mockMvc.perform(get("/rx/comment")).andExpect(status().isOk()).andExpect(view().name("index"));
  }

  @Test
  public void test_get_index_page_via_rx_mix_request() throws Exception {
    mockMvc.perform(get("/rx/mix")).andExpect(status().isOk()).andExpect(view().name("index"));
  }

  @Test
  public void test_get_index_page_via_rx_post_comment_request() throws Exception {
    mockMvc.perform(get("/rx/post/comment")).andExpect(status().isOk()).andExpect(view().name("index"));
  }
}