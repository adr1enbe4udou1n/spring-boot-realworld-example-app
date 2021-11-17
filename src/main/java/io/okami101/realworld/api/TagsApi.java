package io.okami101.realworld.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.okami101.realworld.application.article.TagsResponse;
import io.okami101.realworld.application.article.TagsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Tags")
@RequestMapping(path = "/tags")
public class TagsApi {

    @Autowired
    public TagsService service;

    @GetMapping
    @Operation(summary = "Get tags", description = "Get tags. Auth not required")
    public TagsResponse list() {
        return new TagsResponse(service.getAllTagNames());
    }
}
