package io.okami101.realworld.cli;

import com.github.javafaker.Faker;
import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.ArticleRepository;
import io.okami101.realworld.core.article.Comment;
import io.okami101.realworld.core.article.CommentRepository;
import io.okami101.realworld.core.article.Tag;
import io.okami101.realworld.core.article.TagRepository;
import io.okami101.realworld.core.service.SlugService;
import io.okami101.realworld.core.user.PasswordHashService;
import io.okami101.realworld.core.user.User;
import io.okami101.realworld.core.user.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeederCommand implements CommandLineRunner {

  @Autowired protected UserRepository userRepository;

  @Autowired protected ArticleRepository articleRepository;

  @Autowired protected TagRepository tagRepository;

  @Autowired protected CommentRepository commentRepository;

  @Autowired protected PasswordHashService passwordHashService;

  @Autowired protected SlugService slugService;

  @Autowired protected Flyway flyway;

  @Override
  @Transactional
  public void run(String... args) {
    if (args.length == 0 || !args[0].equals("--seed")) {
      return;
    }

    flyway.clean();
    flyway.migrate();

    Faker faker = new Faker();

    String password = passwordHashService.hash("password");

    for (int i = 0; i < 50; i++) {
      User user = new User();
      user.setName(faker.name().fullName());
      user.setPassword(password);
      user.setEmail(faker.internet().emailAddress());
      user.setBio(faker.lorem().paragraphs(3).stream().collect(Collectors.joining("\n")));
      user.setImage(
          "https://randomuser.me/api/portraits/men/"
              + faker.number().numberBetween(1, 99)
              + ".jpg");

      userRepository.save(user);
    }

    List<User> users = userRepository.findAll();

    users.forEach(
        user -> {
          for (int i = 0; i < faker.number().numberBetween(1, 3); i++) {
            user.getFollowers().add(users.get(faker.number().numberBetween(0, users.size() - 1)));
            userRepository.save(user);
          }
        });

    for (int i = 0; i < 30; i++) {
      tagRepository.save(new Tag(faker.lorem().words(2).stream().collect(Collectors.joining(" "))));
    }

    List<Tag> tags = tagRepository.findAll();

    for (int i = 0; i < 500; i++) {
      String title = faker.lorem().sentence();

      Article article = new Article();
      article.setTitle(title.substring(0, 1).toUpperCase() + title.substring(1));
      article.setSlug(slugService.generate(title));
      article.setDescription(faker.lorem().paragraph());
      article.setBody(faker.lorem().paragraphs(5).stream().collect(Collectors.joining("\n")));
      article.setAuthor(users.get(faker.number().numberBetween(0, 49)));

      for (int j = 0; j < faker.number().numberBetween(1, 3); j++) {
        article.getTags().add(tags.get(faker.number().numberBetween(0, tags.size() - 1)));
      }

      for (int j = 0; j < faker.number().numberBetween(1, 10); j++) {
        article.getFavoritedBy().add(users.get(faker.number().numberBetween(0, users.size() - 1)));
      }

      articleRepository.save(article);

      for (int j = 0; j < faker.number().numberBetween(5, 10); j++) {
        Comment comment = new Comment();
        comment.setBody(faker.lorem().paragraph());
        comment.setAuthor(users.get(faker.number().numberBetween(0, users.size() - 1)));
        comment.setArticle(article);
        commentRepository.save(comment);
      }
    }
  }
}
