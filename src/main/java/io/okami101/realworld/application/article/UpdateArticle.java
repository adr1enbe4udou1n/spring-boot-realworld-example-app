package io.okami101.realworld.application.article;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArticle {
    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotEmpty
    private String body;
}
