package ru.bulatov.phonebook.service;

import org.springframework.stereotype.Service;
import ru.bulatov.phonebook.models.Contact;
import ru.bulatov.phonebook.models.User;

import java.util.List;

@Service
public class PhoneBookService {

    public void addContact(User user, Contact contact){
        user.getPhoneBook().addContact(contact);
    }

    public Contact readContact(User user, int contactId) {
        return user.getPhoneBook().getContact(contactId);
    }

    public Contact findContact(User user, String phoneNumber){
        return user.getPhoneBook().getContact(phoneNumber);
    }

    public List<Contact> getAllContacts(User user){
        return user.getPhoneBook().getAllContacts();
    }

    public boolean updateContact(User user, Contact contact){
        return  user.getPhoneBook().updateContact(contact);
    }

    public boolean deleteContact(User user, int contactId) {
        return user.getPhoneBook().deleteContact(contactId);
    }

}
