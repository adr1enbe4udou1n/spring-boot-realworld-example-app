package io.okami101.realworld.application.article;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.okami101.realworld.core.article.Tag;
import io.okami101.realworld.core.article.TagRepository;

@Service
public class TagsService {

    @Autowired
    private TagRepository tags;

    public List<String> getAllTagNames() {
        return tags.findAllByOrderByNameAsc().stream().map(Tag::getName).collect(Collectors.toList());
    }
}
