package com.levimartines.mylearningbackend.it;

import com.levimartines.mylearningbackend.models.vos.TaskVO;
import com.levimartines.mylearningbackend.models.vos.UserVO;

import java.time.LocalDate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthControllerIntegrationTest extends BaseIntegrationTest {

    @Nested
    class Login {

        @Test
        void shouldReturn401WhenCredentialsAreInvalid() {
            String basePath = "/login";
            var payload = new UserVO("arandomemail@email.com", "someInvalidCredentials");
            var response = template.exchange(basePath, HttpMethod.POST, getEntity(payload, false), Void.class);
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        }
    }
}
