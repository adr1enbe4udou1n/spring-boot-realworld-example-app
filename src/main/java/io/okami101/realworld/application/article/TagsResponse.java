package io.okami101.realworld.application.article;

import java.util.List;

import lombok.Getter;

@Getter
public class TagsResponse {
    private List<String> tags;

    public TagsResponse(List<String> tags) {
        this.tags = tags;
    }
}
