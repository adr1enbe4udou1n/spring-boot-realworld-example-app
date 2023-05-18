package io.okami101.realworld.profile;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.core.user.User;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

public class ProfileFollowTest extends RealworldApplicationTests {
  @Test
  public void guest_cannot_follow_profile() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .post(baseUrl + "/api/profiles/John Doe/follow")
        .then()
        .statusCode(401);
  }

  @Test
  public void cannot_follow_non_existent_profile() {
    actingAsJohnUser()
        .when()
        .post(baseUrl + "/api/profiles/Jane Doe/follow")
        .then()
        .statusCode(401);
  }

  @Test
  public void can_follow_profile() {
    createJaneUser();

    actingAsJohnUser()
        .when()
        .post(baseUrl + "/api/profiles/Jane Doe/follow")
        .then()
        .statusCode(200)
        .body("profile.following", equalTo(true));

    assertTrue(userRepository.findByName("Jane Doe").get().getFollowers().size() == 1);
  }

  @Test
  public void can_unfollow_profile() {
    User john = createJohnUser();
    User jane = createJaneUser();

    john.getFollowers().add(jane);
    userRepository.save(john);

    actingAs(john)
        .when()
        .delete(baseUrl + "/api/profiles/Jane Doe/follow")
        .then()
        .statusCode(200)
        .body("profile.following", equalTo(false));

    assertTrue(userRepository.findByName("Jane Doe").get().getFollowers().size() == 0);
  }
}
