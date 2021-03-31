package ru.bulatov.phonebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.bulatov.phonebook.models.Contact;
import ru.bulatov.phonebook.models.User;
import ru.bulatov.phonebook.service.PhoneBookService;
import ru.bulatov.phonebook.service.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final PhoneBookService phoneBookService;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl, PhoneBookService phoneBookService) {
        this.userServiceImpl = userServiceImpl;
        this.phoneBookService = phoneBookService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        userServiceImpl.create(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> readAllUsers() {
        List<User> users = userServiceImpl.readAll();

        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> readUser(@PathVariable("id") int id) {
        User user = userServiceImpl.read(id);

        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") int id, @RequestBody User user) {
        boolean updated = userServiceImpl.update(id, user);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
        boolean deleted = userServiceImpl.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/find/{name}")
    public ResponseEntity<User> findUser(@PathVariable("name") String name) {
        User foundUser = userServiceImpl.find(name);

        return foundUser != null
                ? new ResponseEntity<>(foundUser, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/phonebook")
    public ResponseEntity<?> createContact(@PathVariable("id") int id, @RequestBody Contact contact) {
        phoneBookService.addContact(userServiceImpl.getUserById(id), contact);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}/phonebook")
    public ResponseEntity<List<Contact>> readAllContacts(@PathVariable("id") int id) {
        List<Contact> contacts = phoneBookService.getAllContacts(userServiceImpl.getUserById(id));

        return contacts != null && !contacts.isEmpty()
                ? new ResponseEntity<>(contacts, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/phonebook/{contactId}")
    public ResponseEntity<Contact> readContact(@PathVariable("id") int id,
                                            @PathVariable("contactId") int contactId) {
        Contact contact = phoneBookService.readContact(userServiceImpl.getUserById(id), contactId);
        return contact != null
                ? new ResponseEntity<>(contact, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}/phonebook/{contactId}")
    public ResponseEntity<?> updateContact(@PathVariable("id") int id,
                                        @PathVariable("contactId") int contactId,
                                        @RequestBody Contact contact) {
        contact.setId(contactId);
        boolean updated = phoneBookService.updateContact(userServiceImpl.getUserById(id), contact);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{id}/phonebook/{contactId}")
    public ResponseEntity<?> deleteContact(@PathVariable("id") int id,
                                           @PathVariable("contactId") int contactId) {
        boolean deleted = phoneBookService.deleteContact(userServiceImpl.getUserById(id), contactId);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/{id}/phonebook/find/{phoneNumber}")
    public ResponseEntity<Contact> findContact(@PathVariable("id") int id,
                                               @PathVariable("phoneNumber") String phoneNumber) {
        User user = userServiceImpl.read(id);
        Contact contact = phoneBookService.findContact(user, phoneNumber);
        return contact != null
                ? new ResponseEntity<>(contact, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
