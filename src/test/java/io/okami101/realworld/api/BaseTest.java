package io.okami101.realworld.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import io.okami101.realworld.core.user.User;
import io.okami101.realworld.core.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class BaseTest {

    @Value("http://localhost:${local.server.port}")
    protected String baseUrl;

    @Autowired
    protected UserRepository userRepository;

    protected User createJohnUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        return user;
    }
}
