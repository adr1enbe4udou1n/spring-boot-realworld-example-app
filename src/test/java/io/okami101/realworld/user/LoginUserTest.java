package io.okami101.realworld.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.application.user.LoginUser;
import io.okami101.realworld.application.user.LoginUserRequest;
import io.okami101.realworld.core.user.User;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

public class LoginUserTest extends RealworldApplicationTests {

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void cannot_login_with_invalid_data(LoginUser data) {
        createJohnUser();

        given().contentType(ContentType.JSON).body(new LoginUserRequest(data)).when().post(baseUrl + "/api/users/login")
                .then().statusCode(400);
    }

    static Stream<LoginUser> dataProvider() {
        return Stream.of(new LoginUser("jane.doe@example.com", "password"),
                new LoginUser("john.doe@example.com", "badpassword"));
    }

    @Test
    public void can_login() {
        User user = createJohnUserWithPassword();

        String token = given().contentType(ContentType.JSON)
                .body(new LoginUserRequest(new LoginUser("john.doe@example.com", "password"))).when()
                .post(baseUrl + "/api/users/login").then().statusCode(200).body("user.username", equalTo("John Doe"))
                .body("user.email", equalTo("john.doe@example.com")).extract().path("user.token");

        assertEquals(user.getId(), Long.parseLong(jwtService.parse(token).get()));
    }
}
