package io.okami101.realworld.application.article;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class TagResponse {
    private List<String> tags;

    public TagResponse(ArrayList<String> tags) {
        this.tags = tags;
    }
}
