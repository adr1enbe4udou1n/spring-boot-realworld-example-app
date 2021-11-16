package io.okami101.realworld.user;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.application.user.NewUser;
import io.okami101.realworld.application.user.NewUserRequest;
import io.okami101.realworld.core.user.User;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegisterUserTest extends RealworldApplicationTests {

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void cannot_register_with_invalid_data(NewUser data) {
        given().contentType(ContentType.JSON).body(new NewUserRequest(data)).when().post(baseUrl + "/api/users").then()
                .statusCode(400);
    }

    static Stream<NewUser> dataProvider() {
        return Stream.of(new NewUser("john.doe", "John Doe", "password"), new NewUser("john.doe@example.com", "", ""),
                new NewUser("john.doe@example.com", "John Doe", "pass"));
    }

    @Test
    public void cannot_register_twice() {
        createJohnUser();

        given().contentType(ContentType.JSON)
                .body(new NewUserRequest(new NewUser("john.doe@example.com", "John Doe", "password"))).when()
                .post(baseUrl + "/api/users").then().statusCode(400);
    }

    @Test
    public void can_register() {
        String token = given().contentType(ContentType.JSON)
                .body(new NewUserRequest(new NewUser("john.doe@example.com", "John Doe", "password"))).when()
                .post(baseUrl + "/api/users").then().statusCode(200).body("user.username", equalTo("John Doe"))
                .body("user.email", equalTo("john.doe@example.com")).extract().path("user.token");

        Optional<User> user = userRepository.findByEmail("john.doe@example.com");
        assertNotNull(user);
        assertEquals(user.get().getId(), Long.parseLong(jwtService.parse(token).get()));
    }
}
