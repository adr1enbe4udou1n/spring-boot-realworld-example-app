package io.okami101.realworld.core.service;

import com.github.slugify.Slugify;
import org.springframework.stereotype.Service;

@Service
public class SlugService {
  public String generate(String title) {
    Slugify slg = Slugify.builder().build();

    return slg.slugify(title);
  }
}
