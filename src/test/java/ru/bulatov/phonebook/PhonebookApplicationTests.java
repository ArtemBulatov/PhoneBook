package ru.bulatov.phonebook;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.bulatov.phonebook.models.User;
import ru.bulatov.phonebook.service.PhoneBookService;
import ru.bulatov.phonebook.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class PhonebookApplicationTests {

    @Autowired
    private UserService userService;
    @Autowired
    private PhoneBookService phoneBookService;
    @Autowired
    private TestRestTemplate restTemplate;

    @AfterEach
    public void reset() {
        for (User user : userService.readAll())
        userService.delete(user.getId());
    }

    @Test
    void whenUserCreated_thenStatus201() {
        User user = new User();
        user.setName("Tim");
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        user.setName("Антон");
        response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        user.setName("Henri d'Albret");
        response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        user.setName("Николай Римский-Корсаков");
        response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
    }

    @Test
    void userNotCreated_Status400() {//о допустимых значениях имени пользователя см. в README.md

        User user = new User();
        user.setName("");

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        user.setName("tim");
        response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        user.setName("Tim123");
        response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        user.setName("A");
        response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        user.setName("Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"); //51 знак
        response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        user.setName("Tony (Ferguson)");
        response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        user.setName("Connor Mc.Gregor");
        response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void readAllUsers() {
        User user = new User();
        user.setName("Martin");
        User user1 = new User();
        user1.setName("Angela");
        userService.create(user);
        userService.create(user1);
        ResponseEntity<List<User>> response = restTemplate.exchange("/users", HttpMethod.GET, null,
                                                                        new ParameterizedTypeReference<List<User>>() {});
        List<User> userList = response.getBody();
        assertThat(userList, hasSize(2));
        assertThat(userList.get(1).getName(), is("Angela"));
    }

    @Test
    void readUser() {
        User user = new User();
        user.setName("Martin");
        User user1 = new User();
        user1.setName("Angela");
        userService.create(user);
        userService.create(user1);

        User angela = restTemplate.getForObject("/users/{id}", User.class, 2);
        assertThat(angela.getName(), is("Angela"));
    }

    @Test
    void updateUser() {
        User user = new User();
        user.setName("Rafail");
        userService.create(user);
        User user1 = new User();
        user1.setName("Michail");
        HttpEntity<User> entity = new HttpEntity<>(user1);
        int id = userService.find("Rafail").getId();
        ResponseEntity<User> response = restTemplate.exchange("/users/{id}", HttpMethod.PUT, entity, User.class, id);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(userService.read(id).getName(), is("Michail"));
    }

    @Test
    void deleteUser() {
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setName("Martin");
        User user1 = new User();
        user1.setName("Angela");
        userService.create(user);
        userService.create(user1);
        int id = userService.find("Martin").getId();
        user.setId(1);
        user1.setId(2);
        userList.add(user);
        userList.add(user1);
        ResponseEntity<User> response = restTemplate.exchange("/users/{id}", HttpMethod.DELETE, null, User.class, id);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        userList.remove(0);
        Assertions.assertEquals(userList, userService.readAll());
    }

    @Test
    void findUser() {
    }

    @Test
    void createContact() {
    }

    @Test
    void readAllContacts() {
    }

    @Test
    void readContact() {
    }

    @Test
    void updateContact() {
    }

    @Test
    void deleteContact() {
    }

    @Test
    void findContact() {
    }
}
