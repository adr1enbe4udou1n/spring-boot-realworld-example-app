package io.okami101.realworld.api.user;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import io.okami101.realworld.api.AuthApi;
import io.okami101.realworld.application.user.NewUser;
import io.okami101.realworld.application.user.NewUserRequest;
import io.okami101.realworld.core.service.JwtService;
import io.okami101.realworld.core.user.PasswordHashService;
import io.okami101.realworld.core.user.UserRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@WebMvcTest(AuthApi.class)
public class RegisterUserTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordHashService passwordHashService;

    @BeforeEach
    public void setUp() throws Exception {
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @ParameterizedTest
    @MethodSource("stringProvider")
    public void cannot_register_with_invalid_data(NewUser data) {
        given().contentType("application/json").body(new NewUserRequest(data)).when().post("/users").then()
                .statusCode(400);
    }

    static Stream<NewUser> stringProvider() {
        return Stream.of(new NewUser("john.doe", "John Doe", "password"), new NewUser("john.doe@example.com", "", ""),
                new NewUser("john.doe@example.com", "John Doe", "pass"));
    }

    @Test
    public void cannot_register_twice() {

    }
}
