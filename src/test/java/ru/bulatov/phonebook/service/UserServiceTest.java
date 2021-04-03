package ru.bulatov.phonebook.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bulatov.phonebook.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserServiceTest {

    private UserService userService;
    private List<User> userList;

    @BeforeEach
    public void setUp() {
        userService = new UserService();
        userList = new ArrayList<>();

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        user1.setId(1);
        user1.setName("Tom");
        user2.setId(2);
        user2.setName("Jerry");
        user3.setId(3);
        user3.setName("Ben");
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        user1 = new User();
        user2 = new User();
        user3 = new User();

        user1.setName("Tom");
        user2.setName("Jerry");
        user3.setName("Ben");
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
    }

    @Test
    void readAll() {
        Assertions.assertEquals(userList, userService.readAll());
    }

    @Test
    void read() {
        Assertions.assertEquals(userList.get(0), (userService.read(1)));
        Assertions.assertEquals(userList.get(1), (userService.read(2)));
        Assertions.assertEquals(userList.get(2), (userService.read(3)));
    }

    @Test
    void update() {
        User user = new User();
        user.setName("Another Tom");
        userList.get(0).setName("Another Tom");
        assert(userService.update(1, user));
        Assertions.assertEquals(userList.get(0), userService.read(1));

        User user2 = new User();
        user2.setName("Another Jerry");
        assert (userService.update(2,user2));
        Assertions.assertNotEquals(userList.get(1), (userService.read(2)));

    }

    @Test
    void delete() {
        userList.remove(1);
        userService.delete(2);
        Assertions.assertEquals(userList, userService.readAll());
    }

    @Test
    void find() {
        Assertions.assertEquals(userList.get(0), userService.find("Tom"));
        Assertions.assertEquals(userList.get(1), userService.find("Jerry"));
        Assertions.assertEquals(userList.get(2), userService.find("Ben"));
    }

}