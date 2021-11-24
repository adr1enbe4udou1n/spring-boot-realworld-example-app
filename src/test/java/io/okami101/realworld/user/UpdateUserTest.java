package io.okami101.realworld.user;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.application.user.UpdateUser;
import io.okami101.realworld.application.user.UpdateUserRequest;
import io.okami101.realworld.core.user.User;
import io.restassured.http.ContentType;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class UpdateUserTest extends RealworldApplicationTests {
  @Test
  public void guest_cannot_update_infos() {
    given().contentType(ContentType.JSON).when().put(baseUrl + "/api/user").then().statusCode(401);
  }

  @ParameterizedTest
  @MethodSource("dataProvider")
  public void cannot_update_infos_with_invalid_data(UpdateUser data) {
    actingAsJohnUser()
        .body(new UpdateUserRequest(data))
        .when()
        .put(baseUrl + "/api/user")
        .then()
        .statusCode(400);
  }

  static Stream<UpdateUser> dataProvider() {
    return Stream.of(
        new UpdateUser(
            "john.doe", "John Doe", "My Bio", "https://randomuser.me/api/portraits/men/1.jpg"));
  }

  @Test
  public void cannot_update_infos_with_already_used_email() {
    createJaneUser();

    actingAsJohnUser()
        .body(new UpdateUserRequest(new UpdateUser("jane.doe@example.com", "John Doe", "", "")))
        .when()
        .put(baseUrl + "/api/user")
        .then()
        .statusCode(400);
  }

  @Test
  public void can_update_infos() {
    actingAsJohnUser()
        .body(
            new UpdateUserRequest(
                new UpdateUser(
                    "john.doe@example.com",
                    "John Doe",
                    "My New Bio",
                    "https://randomuser.me/api/portraits/men/2.jpg")))
        .when()
        .put(baseUrl + "/api/user")
        .then()
        .statusCode(200)
        .body("user.username", equalTo("John Doe"))
        .body("user.email", equalTo("john.doe@example.com"))
        .body("user.bio", equalTo("My New Bio"))
        .body("user.image", equalTo("https://randomuser.me/api/portraits/men/2.jpg"));

    Optional<User> user = userRepository.findByEmail("john.doe@example.com");
    assertNotNull(user);
    assertEquals(user.get().getBio(), "My New Bio");
  }
}
