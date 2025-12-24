package io.okami101.realworld.infrastructure.transaction;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MigrationConfiguration {
  @Bean
  public Flyway flyway(
      @Value("${spring.datasource.url}") String url,
      @Value("${spring.datasource.username}") String user,
      @Value("${spring.datasource.password}") String password) {

    Flyway flyway = Flyway.configure().dataSource(url, user, password).load();

    flyway.migrate();

    return flyway;
  }
}
