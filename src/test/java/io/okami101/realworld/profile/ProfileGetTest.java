package io.okami101.realworld.profile;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.core.user.User;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

public class ProfileGetTest extends RealworldApplicationTests {

  @Test
  public void cannot_get_non_existent_profile() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .get(baseUrl + "/api/profiles/celeb_John Doe")
        .then()
        .statusCode(401);
  }

  @Test
  public void can_get_profile() {
    createJohnUser();

    given()
        .contentType(ContentType.JSON)
        .when()
        .get(baseUrl + "/api/profiles/celeb_John Doe")
        .then()
        .statusCode(200)
        .body("profile.username", equalTo("John Doe"))
        .body("profile.bio", equalTo("John Bio"))
        .body("profile.image", equalTo("https://randomuser.me/api/portraits/men/1.jpg"))
        .body("profile.following", equalTo(false));
  }

  @Test
  public void can_get_followed_profile() {
    User john = createJohnUser();
    User jane = createJaneUser();

    john.getFollowers().add(jane);
    userRepository.save(john);

    actingAs(jane)
        .when()
        .get(baseUrl + "/api/profiles/celeb_John Doe")
        .then()
        .statusCode(200)
        .body("profile.username", equalTo("John Doe"))
        .body("profile.bio", equalTo("John Bio"))
        .body("profile.image", equalTo("https://randomuser.me/api/portraits/men/1.jpg"))
        .body("profile.following", equalTo(true));
  }
}
