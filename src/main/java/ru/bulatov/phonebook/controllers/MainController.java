package ru.bulatov.phonebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bulatov.phonebook.models.Contact;
import ru.bulatov.phonebook.models.User;
import ru.bulatov.phonebook.service.PhoneBookService;
import ru.bulatov.phonebook.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PhoneBookService phoneBookService;

    @Autowired
    public UserController(UserService userService, PhoneBookService phoneBookService) {
        this.userService = userService;
        this.phoneBookService = phoneBookService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid User user) {
        userService.create(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> readAllUsers() {
        List<User> users = userService.readAll();

        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> readUser(@PathVariable("id") int id) {
        User user = userService.read(id);

        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") int id,
                                        @RequestBody @Valid User user) {
        boolean updated = userService.update(id, user);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
        boolean deleted = userService.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/find/{name}")
    public ResponseEntity<User> findUser(@PathVariable("name") String name) {
        User foundUser = userService.find(name);

        return foundUser != null
                ? new ResponseEntity<>(foundUser, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/phonebook")
    public ResponseEntity<?> createContact(@PathVariable("id") int id,
                                           @RequestBody @Valid Contact contact) {
        phoneBookService.addContact(userService.getUserById(id), contact);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}/phonebook")
    public ResponseEntity<List<Contact>> readAllContacts(@PathVariable("id") int id) {
        List<Contact> contacts = phoneBookService.getAllContacts(userService.getUserById(id));

        return contacts != null && !contacts.isEmpty()
                ? new ResponseEntity<>(contacts, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/phonebook/{contactId}")
    public ResponseEntity<Contact> readContact(@PathVariable("id") int id,
                                            @PathVariable("contactId") int contactId) {
        Contact contact = phoneBookService.readContact(userService.getUserById(id), contactId);
        return contact != null
                ? new ResponseEntity<>(contact, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}/phonebook/{contactId}")
    public ResponseEntity<?> updateContact(@PathVariable("id") int id,
                                        @PathVariable("contactId") int contactId,
                                        @RequestBody @Valid Contact contact) {
        contact.setId(contactId);
        boolean updated = phoneBookService.updateContact(userService.getUserById(id), contact);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{id}/phonebook/{contactId}")
    public ResponseEntity<?> deleteContact(@PathVariable("id") int id,
                                           @PathVariable("contactId") int contactId) {
        boolean deleted = phoneBookService.deleteContact(userService.getUserById(id), contactId);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/{id}/phonebook/find/{phoneNumber}")
    public ResponseEntity<Contact> findContact(@PathVariable("id") int id,
                                               @PathVariable("phoneNumber") String phoneNumber) {
        User user = userService.read(id);
        Contact contact = phoneBookService.findContact(user, phoneNumber);
        return contact != null
                ? new ResponseEntity<>(contact, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
