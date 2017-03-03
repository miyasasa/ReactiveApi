package com.beagle.config;

import com.beagle.api.RestProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
@EnableConfigurationProperties(value = RestProperties.class)
public class RestTemplateConfig {

  private RestProperties properties;

  public RestTemplateConfig(RestProperties properties) {
    this.properties = properties;
  }

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
    restTemplate.setMessageConverters(getMessageConverters());
    return restTemplate;
  }

  private ClientHttpRequestFactory clientHttpRequestFactory() {
    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

    requestFactory.setOutputStreaming(false);
    requestFactory.setReadTimeout(properties.getReadTimeout());
    requestFactory.setConnectTimeout(properties.getConnectionTimeout());

    return new BufferingClientHttpRequestFactory(requestFactory);
  }

  private List<HttpMessageConverter<?>> getMessageConverters() {
    List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

    Jaxb2RootElementHttpMessageConverter jaxbMessageConverter = new Jaxb2RootElementHttpMessageConverter();

    List<MediaType> mediaTypes = new ArrayList<>();
    mediaTypes.add(APPLICATION_JSON);

    jaxbMessageConverter.setSupportedMediaTypes(mediaTypes);
    messageConverters.add(new MappingJackson2HttpMessageConverter());

    return messageConverters;
  }
}
