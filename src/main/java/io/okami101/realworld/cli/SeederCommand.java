package io.okami101.realworld.cli;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.okami101.realworld.core.user.PasswordHashService;
import io.okami101.realworld.core.user.User;
import picocli.CommandLine.Command;

@SpringBootApplication
@Command(name = "seederCommand")
public class SeederCommand implements Callable<Integer> {

    @Autowired
    protected EntityManager em;

    @Autowired
    protected PasswordHashService passwordHashService;

    @Autowired
    protected Flyway flyway;

    @Override
    @Transactional
    public Integer call() throws Exception {
        flyway.clean();
        flyway.migrate();

        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setBio("John Bio");
        user.setImage("https://randomuser.me/api/portraits/men/1.jpg");

        em.persist(user);
        em.flush();

        return 0;
    }

}
