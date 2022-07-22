package com.levimartines.mylearningbackend.it;

import com.levimartines.mylearningbackend.models.dtos.TaskDTO;
import com.levimartines.mylearningbackend.models.entities.Task;
import com.levimartines.mylearningbackend.models.vos.TaskVO;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;


public class TaskControllerIntegrationTest extends BaseIntegrationTest {

    private final String basePath = "/tasks";

    @Nested
    class Create {

        @Test
        void shouldCreateWhenPayloadIsValid() {
            TaskVO payload = new TaskVO("create a test", LocalDate.of(2022, 1, 1));
            var response = create(payload);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }

        @Test
        void shouldNotCreateWhenDescriptionIsNullOrEmpty() {
            TaskVO payload = new TaskVO(null, LocalDate.of(2022, 1, 1));
            var response = create(payload);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            payload = new TaskVO("", LocalDate.of(2022, 1, 1));
            response = create(payload);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        void shouldNotCreateWhenDueDateIsNull() {
            TaskVO payload = new TaskVO("create a test", null);
            var response = create(payload);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Nested
    class FindAll {

        @Test
        void shouldReturn200AndAllTasksFromLoggedUser() {
            var userTask1 = Task.builder().description("userTask1").dueDate(LocalDate.now()).user(loggedUser).build();
            var userTask2 = Task.builder().description("userTask2").dueDate(LocalDate.now()).user(loggedUser).build();
            var adminTask1 = Task.builder().description("adminTask1").dueDate(LocalDate.now()).user(loggedAdmin).build();
            taskRepository.saveAll(List.of(userTask1, userTask2, adminTask1));

            var response = template.exchange(basePath, HttpMethod.GET, getEntity(false), TaskDTO[].class);
            assertNotNull(response.getBody());

            var tasks = List.of(response.getBody());
            assertThat(tasks)
                .hasSize(2)
                .extracting(TaskDTO::getDescription)
                .containsExactlyInAnyOrder("userTask1", "userTask2");
        }
    }

    private ResponseEntity<Void> create(TaskVO payload) {
        return template.exchange(basePath, HttpMethod.POST, getEntity(payload, true), Void.class);
    }
}
