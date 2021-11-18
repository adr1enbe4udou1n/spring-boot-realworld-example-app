package io.okami101.realworld.application.article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArticle {
    private String title;

    private String description;

    private String body;
}
