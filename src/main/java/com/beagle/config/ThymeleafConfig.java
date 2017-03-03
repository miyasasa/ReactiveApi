package com.beagle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;

@Configuration
@EnableWebMvc
public class ThymeleafConfig extends WebMvcConfigurerAdapter {

  @Bean
  public SpringResourceTemplateResolver templateResolver() {
    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();

    templateResolver.setPrefix("classpath:/templates/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode("HTML5");
    templateResolver.setCharacterEncoding("UTF-8");
    templateResolver.setOrder(1);

    return templateResolver;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
  }
}
