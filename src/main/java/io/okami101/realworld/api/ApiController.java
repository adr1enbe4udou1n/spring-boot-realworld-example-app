package io.okami101.realworld.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(servers = {@Server(url = "/api")})
public class ApiController {
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(
            new Components()
                .addSecuritySchemes(
                    "Bearer",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .scheme("token")
                        .name("Authorization")
                        .in(In.HEADER)
                        .bearerFormat("JWT")));
  }

  @Bean
  public OpenApiCustomiser sortTagsAlphabetically() {
    return openApi ->
        openApi.setTags(
            openApi.getTags().stream()
                .sorted(Comparator.comparing(Tag::getName))
                .collect(Collectors.toList()));
  }
}
