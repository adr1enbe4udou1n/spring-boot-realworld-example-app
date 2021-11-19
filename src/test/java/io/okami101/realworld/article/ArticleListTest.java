package io.okami101.realworld.article;

import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.Tag;
import io.okami101.realworld.core.user.User;
import io.restassured.http.ContentType;

import static org.hamcrest.core.IsEqual.equalTo;

public class ArticleListTest extends RealworldApplicationTests {

    private User createArticles() {
        Tag tag1 = tagRepository.save(new Tag("Tag 1"));
        Tag tag2 = tagRepository.save(new Tag("Tag 2"));
        Tag johnTag = tagRepository.save(new Tag("John Tag"));
        Tag janeTag = tagRepository.save(new Tag("Jane Tag"));

        User john = createJohnUser();
        User jane = createJaneUser();

        jane.getFollowers().add(john);
        userRepository.save(jane);

        List<String> johnFavoritedArticles = Arrays.asList(new String[] { "jane-article-1", "jane-article-2",
                "jane-article-4", "jane-article-8", "jane-article-16" });

        for (int i = 1; i <= 30; i++) {
            Article article = new Article();
            article.setTitle("John Article " + i);
            article.setSlug("john-article-" + i);
            article.setDescription("Test Description");
            article.setBody("Test Body");
            article.setAuthor(john);
            article.getTags().add(tag1);
            article.getTags().add(tag2);
            article.getTags().add(johnTag);
            articleRepository.save(article);
        }

        for (int i = 1; i <= 20; i++) {
            Article article = new Article();
            article.setTitle("Jane Article " + i);
            article.setSlug("jane-article-" + i);
            article.setDescription("Test Description");
            article.setBody("Test Body");
            article.setAuthor(jane);
            article.getTags().add(tag1);
            article.getTags().add(tag2);
            article.getTags().add(janeTag);

            if (johnFavoritedArticles.contains(article.getSlug())) {
                article.getFavoritedBy().add(john);
            }

            articleRepository.save(article);
        }

        return john;
    }

    @Test
    public void can_paginate_article() {
        createArticles();

        given().contentType(ContentType.JSON).when().get(baseUrl + "/api/articles?limit=10&offset=20").then()
                .statusCode(200).body("articles.size()", equalTo(10))
                .body("articles[0].title", equalTo("John Article 30"))
                .body("articles[0].slug", equalTo("john-article-30"))
                .body("articles[0].description", equalTo("Test Description"))
                .body("articles[0].body", equalTo("Test Body")).body("articles[0].author.username", equalTo("John Doe"))
                .body("articles[0].author.bio", equalTo("John Bio"))
                .body("articles[0].author.image", equalTo("https://randomuser.me/api/portraits/men/1.jpg"))
                .body("articles[0].author.following", equalTo(false)).body("articles[0].favorited", equalTo(false))
                .body("articles[0].favoritesCount", equalTo(0))
                .body("articles[0].tagList", equalTo(Arrays.asList(new String[] { "John Tag", "Tag 1", "Tag 2" })))
                .body("articlesCount", equalTo(50));
    }

    @Test
    public void can_filter_articles_by_author() {
        createArticles();

        given().contentType(ContentType.JSON).when().get(baseUrl + "/api/articles?limit=10&offset=0&author=john").then()
                .statusCode(200).body("articles.size()", equalTo(10))
                .body("articles[0].title", equalTo("John Article 30"))
                .body("articles[0].slug", equalTo("john-article-30"))
                .body("articles[0].description", equalTo("Test Description"))
                .body("articles[0].body", equalTo("Test Body")).body("articles[0].author.username", equalTo("John Doe"))
                .body("articles[0].author.bio", equalTo("John Bio"))
                .body("articles[0].author.image", equalTo("https://randomuser.me/api/portraits/men/1.jpg"))
                .body("articles[0].author.following", equalTo(false)).body("articles[0].favorited", equalTo(false))
                .body("articles[0].favoritesCount", equalTo(0))
                .body("articles[0].tagList", equalTo(Arrays.asList(new String[] { "John Tag", "Tag 1", "Tag 2" })))
                .body("articlesCount", equalTo(30));
    }

    @Test
    public void can_filter_articles_by_tag() {
        createArticles();

        given().contentType(ContentType.JSON).when().get(baseUrl + "/api/articles?limit=10&offset=0&tag=jane").then()
                .statusCode(200).body("articles.size()", equalTo(10))
                .body("articles[0].title", equalTo("Jane Article 20"))
                .body("articles[0].slug", equalTo("jane-article-20"))
                .body("articles[0].description", equalTo("Test Description"))
                .body("articles[0].body", equalTo("Test Body")).body("articles[0].author.username", equalTo("Jane Doe"))
                .body("articles[0].author.bio", equalTo("Jane Bio"))
                .body("articles[0].author.image", equalTo("https://randomuser.me/api/portraits/women/1.jpg"))
                .body("articles[0].author.following", equalTo(false)).body("articles[0].favorited", equalTo(false))
                .body("articles[0].favoritesCount", equalTo(0))
                .body("articles[0].tagList", equalTo(Arrays.asList(new String[] { "Jane Tag", "Tag 1", "Tag 2" })))
                .body("articlesCount", equalTo(20));
    }

    @Test
    public void can_filter_articles_by_favorited() {
        User user = createArticles();

        actingAs(user).contentType(ContentType.JSON).when()
                .get(baseUrl + "/api/articles?limit=10&offset=0&favorited=john").then().statusCode(200)
                .body("articles.size()", equalTo(5)).body("articles[0].title", equalTo("Jane Article 16"))
                .body("articles[0].slug", equalTo("jane-article-16"))
                .body("articles[0].description", equalTo("Test Description"))
                .body("articles[0].body", equalTo("Test Body")).body("articles[0].author.username", equalTo("Jane Doe"))
                .body("articles[0].author.bio", equalTo("Jane Bio"))
                .body("articles[0].author.image", equalTo("https://randomuser.me/api/portraits/women/1.jpg"))
                .body("articles[0].author.following", equalTo(true)).body("articles[0].favorited", equalTo(true))
                .body("articles[0].favoritesCount", equalTo(1))
                .body("articles[0].tagList", equalTo(Arrays.asList(new String[] { "Jane Tag", "Tag 1", "Tag 2" })))
                .body("articlesCount", equalTo(5));
    }

    @Test
    public void guest_cannot_paginate_feed() {
        given().contentType(ContentType.JSON).when().get(baseUrl + "/api/articles/feed").then().statusCode(401);
    }

    @Test
    public void can_paginate_feed() {
        User user = createArticles();

        actingAs(user).contentType(ContentType.JSON).when().get(baseUrl + "/api/articles/feed?limit=10&offset=0").then()
                .statusCode(200).body("articles.size()", equalTo(10))
                .body("articles[0].title", equalTo("Jane Article 20"))
                .body("articles[0].slug", equalTo("jane-article-20"))
                .body("articles[0].description", equalTo("Test Description"))
                .body("articles[0].body", equalTo("Test Body")).body("articles[0].author.username", equalTo("Jane Doe"))
                .body("articles[0].author.bio", equalTo("Jane Bio"))
                .body("articles[0].author.image", equalTo("https://randomuser.me/api/portraits/women/1.jpg"))
                .body("articles[0].author.following", equalTo(true)).body("articles[0].favorited", equalTo(false))
                .body("articles[0].favoritesCount", equalTo(0))
                .body("articles[0].tagList", equalTo(Arrays.asList(new String[] { "Jane Tag", "Tag 1", "Tag 2" })))
                .body("articlesCount", equalTo(20));
    }
}
