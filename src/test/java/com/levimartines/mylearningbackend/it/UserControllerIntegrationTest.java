package com.levimartines.mylearningbackend.it;

import com.levimartines.mylearningbackend.models.dtos.UserDTO;
import com.levimartines.mylearningbackend.models.entities.User;
import com.levimartines.mylearningbackend.models.vos.UserVO;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerIntegrationTest extends BaseIntegrationTest {

    private final String basePath = "/users";

    @Nested
    class FindById {

        @Test
        void shouldReturn200WhenLoggedIsAnAdmin() {
            String url = basePath + "/" + loggedUser.getId();
            HttpEntity<?> entity = getEntity(true);
            ResponseEntity<User> response = template.exchange(url, HttpMethod.GET, entity, User.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn200WhenLoggedAndRequestedUserAreTheSame() {
            String url = basePath + "/" + loggedUser.getId();
            HttpEntity<?> entity = getEntity(false);
            ResponseEntity<User> response = template.exchange(url, HttpMethod.GET, entity, User.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn404WhenUserNotFound() {
            String url = basePath + "/9999";
            HttpEntity<?> entity = getEntity(true);
            ResponseEntity<User> response = template.exchange(url, HttpMethod.GET, entity, User.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        void shouldReturn404WhenLoggedAndRequestedUserAreNotTheSameAndUserIsNotAnAdmin() {
            String url = basePath + "/" + loggedAdmin.getId();
            HttpEntity<?> entity = getEntity(false);
            ResponseEntity<User> response = template.exchange(url, HttpMethod.GET, entity, User.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    class FindAll {
        @Test
        void shouldReturn200IfLoggedUserIsAnAdmin() {
            HttpEntity<?> entity = getEntity(true);
            var response = template.exchange(basePath, HttpMethod.GET, entity, User[].class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().length > 0);
        }

        @Test
        void shouldReturn403IfLoggedUserIsNotAnAdmin() {
            HttpEntity<?> entity = getEntity(false);
            var response = template.exchange(basePath, HttpMethod.GET, entity, User[].class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().length > 0);
        }
    }

    @Nested
    class Create {

        @Test
        void shouldCreateWhenPayloadIsValid() {
            UserVO payload = new UserVO("newuser@users.com", "mypassword");
            var response = template.postForEntity(basePath, payload, UserDTO.class);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }

        @Test
        void shouldNotCreateWithDuplicatedEmail() {
            UserVO payload = new UserVO(loggedAdmin.getEmail(), "mypassword");
            var response = template.postForEntity(basePath, payload, UserDTO.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        void shouldNotCreateWhenPayloadEmailIsInvalid() {
            UserVO payload = new UserVO("newuser", "mypassword");
            var response = template.postForEntity(basePath, payload, UserDTO.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        void shouldNotCreateWhenPayloadEmailOrPasswordIsNullOrEmpty() {
            UserVO payload = new UserVO();
            var response = template.postForEntity(basePath, payload, UserDTO.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            payload = new UserVO("", "");
            response = template.postForEntity(basePath, payload, UserDTO.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }
}
