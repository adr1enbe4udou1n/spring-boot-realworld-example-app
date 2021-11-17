package io.okami101.realworld.article;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.okami101.realworld.RealworldApplicationTests;
import io.restassured.http.ContentType;

public class ArticleListTest extends RealworldApplicationTests {
    @Test
    public void can_paginate_article() {
    }

    @Test
    public void can_filter_articles_by_author() {
    }

    @Test
    public void can_filter_articles_by_tag() {
    }

    @Test
    public void can_filter_articles_by_favorited() {
    }

    @Test
    public void guest_cannot_paginate_feed() {
        given().contentType(ContentType.JSON).when().get(baseUrl + "/api/articles/feed").then().statusCode(401);
    }

    @Test
    public void can_paginate_feed() {
    }
}
