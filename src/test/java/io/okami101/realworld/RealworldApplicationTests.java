package io.okami101.realworld;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import io.okami101.realworld.core.service.JwtService;
import io.okami101.realworld.core.user.PasswordHashService;
import io.okami101.realworld.core.user.User;
import io.okami101.realworld.core.user.UserRepository;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class RealworldApplicationTests {

    @Value("http://localhost:${local.server.port}")
    protected String baseUrl;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordHashService passwordHashService;

    @Autowired
    protected JwtService jwtService;

    @Autowired
    protected Flyway flyway;

    @BeforeEach
    public void cleanUp() {
        flyway.clean();
        flyway.migrate();
    }

    protected User createJohnUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        return user;
    }

    protected User createJaneUser() {
        User user = new User();
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        userRepository.save(user);

        return user;
    }

    protected User createJohnUserWithPassword() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword(passwordHashService.hash("password"));
        userRepository.save(user);

        return user;
    }

    protected RequestSpecification actingAsJohnUser() {
        return actingAs(createJohnUser());
    }

    protected RequestSpecification actingAs(User user) {
        String token = jwtService.encode(user);

        return given().contentType(ContentType.JSON).header("Authorization", "Token " + token);
    }
}
