package io.okami101.realworld.user;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

import io.okami101.realworld.RealworldApplicationTests;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

public class CurrentUserTest extends RealworldApplicationTests {
  @Test
  public void guest_cannot_fetch_infos() {
    given().contentType(ContentType.JSON).when().get(baseUrl + "/api/user").then().statusCode(401);
  }

  @Test
  public void can_fetch_infos() {
    actingAsJohnUser()
        .when()
        .get(baseUrl + "/api/user")
        .then()
        .statusCode(200)
        .body("user.username", equalTo("John Doe"))
        .body("user.email", equalTo("john.doe@example.com"));
  }
}
