package ru.bulatov.phonebook;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import ru.bulatov.phonebook.models.Contact;
import ru.bulatov.phonebook.models.User;
import ru.bulatov.phonebook.service.PhoneBookService;
import ru.bulatov.phonebook.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @BeforeEach
    void setUp() {
        userService.deleteAllUsers();
    }

    @Test
    void createUser_whenCreatedStatus201() {
        User user = new User();
        user.setName("Tim");
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        user.setName("Антон");
        response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        user.setName("Henri d'Albert");
        response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        user.setName("Николай Римский-Корсаков");
        response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
    }

    @Test
    void createUser_whenNotCreatedStatus400() {//о допустимых значениях имени пользователя см. в README.md

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
        assert(Objects.requireNonNull(userList).size() == 2);
        assertThat(userList.get(1).getName(), is("Angela"));
    }

    @Test
    void readUser() {
        User user = new User();
        user.setName("Martin");
        userService.create(user);
        User user1 = new User();
        user1.setName("Angela");
        userService.create(user1);

        ResponseEntity<User> response = restTemplate.exchange("/users/{id}", HttpMethod.GET, null, User.class, 2);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        response = restTemplate.exchange("/users/{id}", HttpMethod.GET, null, User.class, 4);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
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

        int userId = 5; // пользователь с таким id не существует
        response = restTemplate.exchange("/users/{id}", HttpMethod.PUT, entity, User.class, userId);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_MODIFIED));

        user1.setName("Michail222");
        entity = new HttpEntity<>(user1);
        response = restTemplate.exchange("/users/{id}", HttpMethod.PUT, entity, User.class, id);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
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
        User user = new User();
        user.setName("Николай Римский-Корсаков");
        userService.create(user);
        user.setId(1);

        ResponseEntity<User> response = restTemplate.exchange("/users/find/{nameOrPartOfName}", HttpMethod.GET, null, User.class, "Николай Римский-Корсаков");
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        Assertions.assertEquals(user, response.getBody());

        response = restTemplate.exchange("/users/find/{nameOrPartOfName}", HttpMethod.GET, null, User.class, "Николай");
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        Assertions.assertEquals(user, response.getBody());

        response = restTemplate.exchange("/users/find/{nameOrPartOfName}", HttpMethod.GET, null, User.class, "Римский");
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        Assertions.assertEquals(user, response.getBody());


        response = restTemplate.exchange("/users/find/{nameOrPartOfName}", HttpMethod.GET, null, User.class, "Корсаков");
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        Assertions.assertEquals(user, response.getBody());
    }

    @Test
    void createContact_whenCreatedStatus201() {
        User user = new User();
        user.setName("Bob");
        userService.create(user);
        int userId = userService.find("Bob").getId();

        Contact contact = new Contact();
        contact.setName("Tim");
        contact.setPhoneNumber("8 999 209 10 53");
        HttpEntity<Contact> entity = new HttpEntity<>(contact);
        ResponseEntity<Contact> response = restTemplate.exchange("/users/{userId}/phonebook", HttpMethod.POST, entity, Contact.class, userId);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        contact.setName("Henri d'Albert");
        contact.setPhoneNumber("+7(999)2091053");
        entity = new HttpEntity<>(contact);
        response = restTemplate.exchange("/users/{userId}/phonebook", HttpMethod.POST, entity, Contact.class, userId);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        contact.setName("Николай Римский-Корсаков");
        contact.setPhoneNumber("8(999)209-10-53");
        entity = new HttpEntity<>(contact);
        response = restTemplate.exchange("/users/{userId}/phonebook", HttpMethod.POST, entity, Contact.class, userId);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        contact.setName("Марина Цветаева");
        contact.setPhoneNumber("(812)318-10-53");
        entity = new HttpEntity<>(contact);
        response = restTemplate.exchange("/users/{userId}/phonebook", HttpMethod.POST, entity, Contact.class, userId);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        contact.setName("Герхард фон Рад");
        contact.setPhoneNumber("+50(945)2345577");
        entity = new HttpEntity<>(contact);
        response = restTemplate.exchange("/users/{userId}/phonebook", HttpMethod.POST, entity, Contact.class, userId);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
    }

    @Test
    void createContact_whenNotCreatedStatus400() {
        User user = new User();
        user.setName("Bob");
        userService.create(user);
        int userId = userService.find("Bob").getId();

        Contact contact = new Contact();
        contact.setName("Tim");
        contact.setPhoneNumber("8999");
        HttpEntity<Contact> entity = new HttpEntity<>(contact);
        ResponseEntity<Contact> response = restTemplate.exchange("/users/{userId}/phonebook", HttpMethod.POST, entity, Contact.class, userId);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        contact.setName("Henri d'Albert");
        contact.setPhoneNumber("7+9992091053");
        entity = new HttpEntity<>(contact);
        response = restTemplate.exchange("/users/{userId}/phonebook", HttpMethod.POST, entity, Contact.class, userId);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        contact.setName("Николай Римский-Корсаков");
        contact.setPhoneNumber("8(999209-10-53");
        entity = new HttpEntity<>(contact);
        response = restTemplate.exchange("/users/{userId}/phonebook", HttpMethod.POST, entity, Contact.class, userId);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        contact.setName("Марина Цветаевааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааа");
        contact.setPhoneNumber("(812)318-10-53");
        entity = new HttpEntity<>(contact);
        response = restTemplate.exchange("/users/{userId}/phonebook", HttpMethod.POST, entity, Contact.class, userId);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        contact.setName("");
        contact.setPhoneNumber("+50(945)2345577");
        entity = new HttpEntity<>(contact);
        response = restTemplate.exchange("/users/{userId}/phonebook", HttpMethod.POST, entity, Contact.class, userId);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        contact.setName("Jhon");
        contact.setPhoneNumber("+7[945]45-38-999");
        entity = new HttpEntity<>(contact);
        response = restTemplate.exchange("/users/{userId}/phonebook", HttpMethod.POST, entity, Contact.class, userId);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void readAllContacts() {
        User user = new User();
        user.setName("Bob");
        userService.create(user);
        int userId = userService.find("Bob").getId();

        Contact contact1 = new Contact();
        contact1.setName("Николай Римский-Корсаков");
        contact1.setPhoneNumber("8(999)209-10-53");
        Contact contact2 = new Contact();
        contact2.setName("Марина Цветаева");
        contact2.setPhoneNumber("(812)318-10-53");
        phoneBookService.addContact(userService.read(userId), contact1);
        phoneBookService.addContact(userService.read(userId), contact2);

        ResponseEntity<List<Contact>> response = restTemplate.exchange("/users/{userId}/phonebook", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Contact>>() {}, userId);
        List<Contact> contactList = response.getBody();
        assertThat(contactList, hasSize(2));
        assertThat(contactList.get(0).getName(), is("Николай Римский-Корсаков"));
        assertThat(contactList.get(0).getPhoneNumber(), is("8(999)209-10-53"));
        assertThat(contactList.get(1).getName(), is("Марина Цветаева"));
        assertThat(contactList.get(1).getPhoneNumber(), is("(812)318-10-53"));
    }

    @Test
    void readContact() {
        User user = new User();
        user.setName("Bob");
        userService.create(user);
        int userId = userService.find("Bob").getId();

        Contact contact1 = new Contact();
        contact1.setName("Николай Римский-Корсаков");
        contact1.setPhoneNumber("8(999)209-10-53");
        Contact contact2 = new Contact();
        contact2.setName("Марина Цветаева");
        contact2.setPhoneNumber("(812)318-10-53");
        phoneBookService.addContact(userService.read(userId), contact1);
        phoneBookService.addContact(userService.read(userId), contact2);
        contact1.setId(1);
        contact2.setId(2);
        Contact nikolai = restTemplate.getForObject("/users/{userId}/phonebook/{contactId}",Contact.class, userId, 1);
        Contact marina = restTemplate.getForObject("/users/{userId}/phonebook/{contactId}",Contact.class, userId, 2);
        Assertions.assertEquals(contact1, nikolai);
        Assertions.assertEquals(contact2, marina);
    }

    @Test
    void updateContact() {
        User user = new User();
        user.setName("Bob");
        userService.create(user);
        int userId = userService.find("Bob").getId();

        Contact contact = new Contact();
        contact.setName("Николай Корсаков");
        contact.setPhoneNumber("89992091053");
        phoneBookService.addContact(userService.read(userId), contact);

        Contact contact1 = new Contact();
        contact1.setName("Николай Римский-Корсаков");
        contact1.setPhoneNumber("+7(999)209-10-53");
        HttpEntity<Contact> entity = new HttpEntity<>(contact1);

        ResponseEntity<Contact> response = restTemplate.exchange("/users/{userId}/phonebook/{contactId}", HttpMethod.PUT, entity, Contact.class, userId, 1);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(phoneBookService.readContact(user, 1).getName(), is("Николай Римский-Корсаков"));
        assertThat(phoneBookService.readContact(user, 1).getPhoneNumber(), is("+7(999)209-10-53"));

        response = restTemplate.exchange("/users/{userId}/phonebook/{contactId}",
                HttpMethod.PUT, entity, Contact.class, userId, 5); // контакт с таким id не существует
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_MODIFIED));

        contact1.setName("Николай Римский--Корсаков");
        entity = new HttpEntity<>(contact1);
        response = restTemplate.exchange("/users/{userId}/phonebook/{contactId}", HttpMethod.PUT, entity, Contact.class, userId, 1);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        contact1.setName("Николай Римский-Корсаков");
        contact1.setPhoneNumber("-79995234345");
        entity = new HttpEntity<>(contact1);
        response = restTemplate.exchange("/users/{userId}/phonebook/{contactId}", HttpMethod.PUT, entity, Contact.class, userId, 1);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

    }

    @Test
    void deleteContact() {
        User user = new User();
        user.setName("Bob");
        userService.create(user);
        int userId = userService.find("Bob").getId();

        Contact contact = new Contact();
        contact.setName("Tim");
        contact.setPhoneNumber("8 999 209 10 53");
        Contact contact1 = new Contact();
        contact1.setName("Николай Римский-Корсаков");
        contact1.setPhoneNumber("8(999)209-10-53");
        Contact contact2 = new Contact();
        contact2.setName("Марина Цветаева");
        contact2.setPhoneNumber("(812)318-10-53");
        phoneBookService.addContact(userService.read(userId), contact);
        phoneBookService.addContact(userService.read(userId), contact1);
        phoneBookService.addContact(userService.read(userId), contact2);
        contact.setId(1);
        contact1.setId(2);
        contact2.setId(3);
        List<Contact> contactList = new ArrayList<>();
        contactList.add(contact);
        contactList.add(contact1);
        contactList.add(contact2);

        ResponseEntity<Contact> response = restTemplate.exchange("/users/{userId}/phonebook/{contactId}", HttpMethod.DELETE, null, Contact.class, userId, 2);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        contactList.remove(1);
        Assertions.assertEquals(contactList, phoneBookService.getAllContacts(user));
    }

    @Test
    void findContact() {
        User user = new User();
        user.setName("Bob");
        userService.create(user);
        int userId = userService.find("Bob").getId();

        Contact contact1 = new Contact();
        contact1.setName("Николай Римский-Корсаков");
        contact1.setPhoneNumber("8(999)209-10-53");
        Contact contact2 = new Contact();
        contact2.setName("Марина Цветаева");
        contact2.setPhoneNumber("(812)318-10-53");
        phoneBookService.addContact(userService.read(userId), contact1);
        phoneBookService.addContact(userService.read(userId), contact2);
        contact1.setId(1);
        contact2.setId(2);
        Contact nikolai = restTemplate.getForObject("/users/{userId}/phonebook/find/{phoneNumber}",Contact.class, userId, "89992091053");
        Contact marina = restTemplate.getForObject("/users/{userId}/phonebook/find/{phoneNumber}",Contact.class, userId, "8123181053");
        Assertions.assertEquals(contact1, nikolai);
        Assertions.assertEquals(contact2, marina);
    }
}
