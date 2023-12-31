package com.example.jangdocdaefilm.configuration;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
  @Bean
  public MultipartResolver multipartResolver() {
    return new StandardServletMultipartResolver();
  }

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();

    factory.setMaxRequestSize(DataSize.ofBytes(10 * 1024 * 1024));
    factory.setMaxFileSize(DataSize.ofBytes(10 * 1024 * 1024));
    return factory.createMultipartConfig();
  }
  
  // 외부 파일 경로 불러오기
  @Value("${resource.free.path}")
  private String resourceFreePath;

  @Value("${resource.dis.path}")
  private String resourceDisPath;

  @Value("${resource.qna.path}")
  private String resourceQnaPath;

  @Value("${resource.now.path}")
  private String resourceNowPath;
  //  프로필 이미지 경로(추가)
  @Value("${resource.member.path}")
  private String resourceMemberPath;
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/free/**").addResourceLocations(resourceFreePath);
    registry.addResourceHandler("/dis/**").addResourceLocations(resourceDisPath);
    registry.addResourceHandler("/qna/**").addResourceLocations(resourceQnaPath);
    registry.addResourceHandler("/now/**").addResourceLocations(resourceNowPath);
    //  프로필 이미지(추가)
    registry.addResourceHandler("/member/**").addResourceLocations(resourceMemberPath);

  }


}
