package ru.bulatov.phonebook.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bulatov.phonebook.models.Contact;
import ru.bulatov.phonebook.models.User;

import java.util.ArrayList;
import java.util.List;

class PhoneBookServiceTest {
    private User user;
    private PhoneBookService phoneBookService;
    private List<Contact> contactList;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("TestUser");
        phoneBookService = new PhoneBookService();
        contactList = new ArrayList<>();

        Contact contact1 = new Contact();
        Contact contact2 = new Contact();
        Contact contact3 = new Contact();
        contact1.setId(1);
        contact1.setName("Alice");
        contact1.setPhoneNumber("+19327453539");
        contact2.setId(2);
        contact2.setName("Viktoria");
        contact2.setPhoneNumber("+69(872)347-85-27");
        contact3.setId(3);
        contact3.setName("Maria");
        contact3.setPhoneNumber("89823550733");
        contactList.add(contact1);
        contactList.add(contact2);
        contactList.add(contact3);

        contact1 = new Contact();
        contact2 = new Contact();
        contact3 = new Contact();
        contact1.setName("Alice");
        contact1.setPhoneNumber("+19327453539");
        contact2.setName("Viktoria");
        contact2.setPhoneNumber("+69(872)347-85-27");
        contact3.setName("Maria");
        contact3.setPhoneNumber("89823550733");
        phoneBookService.addContact(user, contact1);
        phoneBookService.addContact(user, contact2);
        phoneBookService.addContact(user, contact3);
    }

    @Test
    void readContact() {
        Assertions.assertEquals(contactList.get(0), phoneBookService.readContact(user, 1));
        Assertions.assertEquals(contactList.get(1), phoneBookService.readContact(user, 2));
        Assertions.assertNotEquals(contactList.get(0), phoneBookService.readContact(user, 3));
    }

    @Test
    void findContact() {
        Assertions.assertEquals(contactList.get(0), phoneBookService.findContact(user, "+19327453539"));
        Assertions.assertEquals(contactList.get(1), phoneBookService.findContact(user, "+69(872)347-85-27"));
        Assertions.assertNotEquals(contactList.get(1), phoneBookService.findContact(user, "89823550733"));
    }

    @Test
    void getAllContacts() {
        Assertions.assertEquals(contactList, phoneBookService.getAllContacts(user));
    }

    @Test
    void updateContact() {
        contactList.get(0).setName("Alice Jonson");
        contactList.get(0).setPhoneNumber("+1(932)74-53-539");

        Contact contact1 = new Contact();
        contact1.setId(1);
        contact1.setName("Alice Jonson");
        contact1.setPhoneNumber("+1(932)74-53-539");
        phoneBookService.updateContact(user, contact1);

        Assertions.assertEquals(contactList.get(0), phoneBookService.readContact(user, 1));

        Contact contact3 = new Contact();
        contact3.setId(3);
        contact3.setName("Masha");
        contact3.setPhoneNumber("89823550733");
        phoneBookService.updateContact(user, contact3);

        Assertions.assertNotEquals(contactList.get(2), phoneBookService.readContact(user, 3));
    }

    @Test
    void deleteContact() {
        contactList.remove(1);
        phoneBookService.deleteContact(user, 2);
        Assertions.assertEquals(contactList, phoneBookService.getAllContacts(user));
    }


}