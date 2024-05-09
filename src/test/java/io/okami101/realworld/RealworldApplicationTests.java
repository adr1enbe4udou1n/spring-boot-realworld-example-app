package io.okami101.realworld;

import static io.restassured.RestAssured.given;

import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.ArticleRepository;
import io.okami101.realworld.core.article.CommentRepository;
import io.okami101.realworld.core.article.TagRepository;
import io.okami101.realworld.core.service.JwtService;
import io.okami101.realworld.core.user.PasswordHashService;
import io.okami101.realworld.core.user.User;
import io.okami101.realworld.core.user.UserRepository;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class RealworldApplicationTests {

  @Value("http://localhost:${local.server.port}")
  protected String baseUrl;

  @Autowired protected UserRepository userRepository;

  @Autowired protected ArticleRepository articleRepository;

  @Autowired protected TagRepository tagRepository;

  @Autowired protected CommentRepository commentRepository;

  @Autowired protected PasswordHashService passwordHashService;

  @Autowired protected JwtService jwtService;

  @Autowired protected Flyway flyway;

  @BeforeEach
  public void migrate() {
    flyway.migrate();
  }

  @AfterEach
  public void cleanup() {
    tagRepository.deleteAll();
    commentRepository.deleteAll();
    articleRepository.deleteAll();
    userRepository.deleteAll();
  }

  protected User createJohnUser() {
    User user = new User();
    user.setName("John Doe");
    user.setEmail("john.doe@example.com");
    user.setBio("John Bio");
    user.setImage("https://randomuser.me/api/portraits/men/1.jpg");
    userRepository.save(user);

    return user;
  }

  protected User createJaneUser() {
    User user = new User();
    user.setName("Jane Doe");
    user.setEmail("jane.doe@example.com");
    user.setBio("Jane Bio");
    user.setImage("https://randomuser.me/api/portraits/women/1.jpg");
    userRepository.save(user);

    return user;
  }

  protected User createJohnUserWithPassword() {
    User user = new User();
    user.setName("John Doe");
    user.setEmail("john.doe@example.com");
    user.setPassword(passwordHashService.hash("password"));
    userRepository.save(user);

    return user;
  }

  protected RequestSpecification actingAsJohnUser() {
    return actingAs(createJohnUser());
  }

  protected RequestSpecification actingAs(User user) {
    String token = jwtService.encode(user);

    return given().contentType(ContentType.JSON).header("Authorization", "Token " + token);
  }

  protected Article createArticle(User user) {
    return createArticleWithSlug(user, "test-title");
  }

  protected Article createArticleWithSlug(User user, String slug) {
    Article article = new Article();
    article.setTitle("Test Title");
    article.setSlug(slug);
    article.setDescription("Test Description");
    article.setBody("Test Body");
    article.setAuthor(user);
    articleRepository.save(article);

    return article;
  }
}
