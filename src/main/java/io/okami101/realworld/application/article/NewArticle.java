package io.okami101.realworld.application.article;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewArticle {
  @NotBlank @DuplicatedSlugConstraint private String title;

  @NotBlank private String description;

  @NotBlank private String body;

  private List<String> tagList;
}
