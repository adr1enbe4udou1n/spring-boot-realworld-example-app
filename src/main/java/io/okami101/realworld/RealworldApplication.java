package io.okami101.realworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class RealworldApplication {

  public static void main(String[] args) {
    if (args.length == 1 && args[0].equals("--seed")) {
      new SpringApplicationBuilder(RealworldApplication.class)
          .web(WebApplicationType.NONE)
          .run(args);
      return;
    }

    SpringApplication.run(RealworldApplication.class, args);
  }
}
