package com.xebia.chatroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author heenanagpal
 *
 */
@SpringBootApplication
public class XebiaChatRoomApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(XebiaChatRoomApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(XebiaChatRoomApplication.class, args);
  }
}
