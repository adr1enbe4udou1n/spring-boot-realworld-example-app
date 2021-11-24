package io.okami101.realworld.tag;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.core.article.Tag;
import io.restassured.http.ContentType;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class TagListTest extends RealworldApplicationTests {

  @Test
  public void can_list_all_tags() {
    tagRepository.save(new Tag("Tag 3"));
    tagRepository.save(new Tag("Tag 2"));
    tagRepository.save(new Tag("Tag 1"));

    given()
        .contentType(ContentType.JSON)
        .when()
        .get(baseUrl + "/api/tags")
        .then()
        .statusCode(200)
        .body("tags", equalTo(Arrays.asList(new String[] {"Tag 1", "Tag 2", "Tag 3"})));
  }
}
