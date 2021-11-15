package io.okami101.realworld.api.user;

import org.springframework.boot.test.mock.mockito.MockBean;

import io.okami101.realworld.core.user.User;
import io.okami101.realworld.core.user.UserRepository;

public class BaseTest {

    @MockBean
    protected UserRepository userRepository;

    protected User createJohnUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        return user;
    }
}
