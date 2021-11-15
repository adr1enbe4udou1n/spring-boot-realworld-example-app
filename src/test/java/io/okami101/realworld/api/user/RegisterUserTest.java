package io.okami101.realworld.api.user;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import io.okami101.realworld.application.user.NewUser;
import io.okami101.realworld.application.user.NewUserRequest;
import io.okami101.realworld.core.service.JwtService;
import io.okami101.realworld.core.user.PasswordHashService;
import io.okami101.realworld.core.user.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegisterUserTest extends BaseTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordHashService passwordHashService;

    @ParameterizedTest
    @MethodSource("stringProvider")
    public void cannot_register_with_invalid_data(NewUser data) {
        given().contentType("application/json").body(new NewUserRequest(data)).when().post(baseUrl + "/api/users")
                .then().statusCode(400);
    }

    static Stream<NewUser> stringProvider() {
        return Stream.of(new NewUser("john.doe", "John Doe", "password"), new NewUser("john.doe@example.com", "", ""),
                new NewUser("john.doe@example.com", "John Doe", "pass"));
    }

    @Test
    public void cannot_register_twice() {
        createJohnUser();

        given().contentType("application/json")
                .body(new NewUserRequest(new NewUser("john.doe@example.com", "John Doe", "password"))).when()
                .post(baseUrl + "/api/users").then().statusCode(400);
    }

    @Test
    public void can_register() {
        String token = given().contentType("application/json")
                .body(new NewUserRequest(new NewUser("john.doe@example.com", "John Doe", "password"))).when()
                .post(baseUrl + "/api/users").then().statusCode(200).body("user.username", equalTo("John Doe"))
                .body("user.email", equalTo("john.doe@example.com")).extract().path("user.token");

        Optional<User> user = userRepository.findByEmail("john.doe@example.com");
        assertNotNull(user);
        assertEquals(user.get().getId(), Long.parseLong(jwtService.parse(token).get()));
    }
}
