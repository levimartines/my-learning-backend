package com.levimartines.mylearningbackend.it;

import com.levimartines.mylearningbackend.Application;
import com.levimartines.mylearningbackend.models.entities.User;
import com.levimartines.mylearningbackend.models.vos.UserVO;
import com.levimartines.mylearningbackend.repositories.TaskRepository;
import com.levimartines.mylearningbackend.repositories.UserRepository;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.levimartines.mylearningbackend.fixtures.UserFixtures.DEFAULT_ADMIN;
import static com.levimartines.mylearningbackend.fixtures.UserFixtures.DEFAULT_USER;
import static org.testcontainers.utility.DockerImageName.parse;

@SpringBootTest(
    classes = {Application.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ContextConfiguration(initializers = {BaseIntegrationTest.DockerMySQLDataSourceInitializer.class})
@Testcontainers
public abstract class BaseIntegrationTest {

    @ClassRule
    public static PostgreSQLContainer<?> postgreContainer = new PostgreSQLContainer<>("postgres:alpine");
    @ClassRule
    public static GenericContainer<?> redis = new GenericContainer<>(parse("redis:alpine"))
        .withExposedPorts(6379);

    static {
        postgreContainer.start();
        redis.start();
    }

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TaskRepository taskRepository;

    @Autowired
    protected TestRestTemplate template;

    @Autowired
    protected BCryptPasswordEncoder encoder;

    protected User loggedAdmin;
    protected User loggedUser;

    @BeforeEach
    public void beforeEach() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        loggedAdmin = saveUser(DEFAULT_ADMIN);
        loggedUser = saveUser(DEFAULT_USER);
    }

    protected User saveUser(User user) {
        User newUser = encodePassword(user);
        userRepository.save(newUser);
        return user.toBuilder().id(newUser.getId()).build();
    }

    private User encodePassword(User user) {
        String encodedPass = encoder.encode(user.getPassword());
        return user.toBuilder().password(encodedPass).build();
    }

    protected HttpEntity<?> getEntity(boolean admin) {
        return getEntity(null, admin);
    }

    protected <T> HttpEntity<?> getEntity(T t, boolean admin) {
        ResponseEntity<Void> response = login(admin);
        List<String> authHeaders = response.getHeaders().get("Authorization");
        if (authHeaders == null || authHeaders.isEmpty()) {
            throw new RuntimeException("Authentication headers should not be null or empty. Please check the logged users");
        }
        String bearerToken = authHeaders.get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", bearerToken);
        return new HttpEntity<>(t, headers);
    }

    protected ResponseEntity<Void> login(boolean admin) {
        User user = admin ? loggedAdmin : loggedUser;
        UserVO form = new UserVO(user.getEmail(), user.getPassword());
        return template.postForEntity("/login", form, Void.class);
    }

    static class DockerMySQLDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url=" + postgreContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgreContainer.getUsername(),
                "spring.datasource.password=" + postgreContainer.getPassword()
            );
        }
    }
}
