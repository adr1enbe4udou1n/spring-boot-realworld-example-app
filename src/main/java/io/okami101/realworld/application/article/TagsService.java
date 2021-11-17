package io.okami101.realworld.application.article;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.okami101.realworld.core.article.TagRepository;

@Service
public class TagsService {

    @Autowired
    private TagRepository tags;

    public List<String> getAllTagNames() {
        return Lists.newArrayList(tags.findAll()).stream().map(t -> t.getName()).collect(Collectors.toList());
    }
}
