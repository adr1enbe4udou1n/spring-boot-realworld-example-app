package io.okami101.realworld.application.article;

import io.okami101.realworld.core.article.Tag;
import io.okami101.realworld.core.article.TagRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagsService {

  private final TagRepository tags;

  public TagsService(TagRepository tags) {
    this.tags = tags;
  }

  @Transactional(readOnly = true)
  public List<String> getAllTagNames() {
    return tags.findAllByOrderByNameAsc().stream().map(Tag::getName).collect(Collectors.toList());
  }
}
