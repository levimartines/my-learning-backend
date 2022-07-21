package com.levimartines.mylearningbackend.it;

import com.levimartines.mylearningbackend.models.dtos.UserDTO;
import com.levimartines.mylearningbackend.models.entities.User;
import com.levimartines.mylearningbackend.models.vos.UserVO;

import org.junit.jupiter.api.Assertions;
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

    @Nested
    class FindById {

        @Test
        void shouldReturn200WhenLoggedIsAnAdmin() {
            String url = "/users/" + loggedUser.getId();
            HttpEntity<?> entity = getEntity(true);
            ResponseEntity<User> response = template.exchange(url, HttpMethod.GET, entity, User.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn200WhenLoggedAndRequestedUserAreTheSame() {
            String url = "/users/" + loggedUser.getId();
            HttpEntity<?> entity = getEntity(false);
            ResponseEntity<User> response = template.exchange(url, HttpMethod.GET, entity, User.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn404WhenUserNotFound() {
            String url = "/users/9999";
            HttpEntity<?> entity = getEntity(true);
            ResponseEntity<User> response = template.exchange(url, HttpMethod.GET, entity, User.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        void shouldReturn404WhenLoggedAndRequestedUserAreNotTheSameAndUserIsNotAnAdmin() {
            String url = "/users/" + loggedAdmin.getId();
            HttpEntity<?> entity = getEntity(false);
            ResponseEntity<User> response = template.exchange(url, HttpMethod.GET, entity, User.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    class FindAll {
        @Test
        void shouldReturn200IfLoggedUserIsAnAdmin() {
            String url = "/users";
            HttpEntity<?> entity = getEntity(true);
            var response = template.exchange(url, HttpMethod.GET, entity, User[].class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().length > 0);
        }

        @Test
        void shouldReturn403IfLoggedUserIsNotAnAdmin() {
            String url = "/users";
            HttpEntity<?> entity = getEntity(false);
            var response = template.exchange(url, HttpMethod.GET, entity, User[].class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().length > 0);
        }
    }

    @Nested
    class Create {

        @Test
        void shouldCreateWhenPayloadIsValid() {
            String url = "/users";
            UserVO payload = new UserVO("newuser@users.com", "mypassword");
            var response = template.postForEntity(url, payload, UserDTO.class);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }
}
