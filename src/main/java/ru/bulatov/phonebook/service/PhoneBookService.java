package ru.bulatov.phonebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bulatov.phonebook.models.Contact;
import ru.bulatov.phonebook.models.PhoneBook;
import ru.bulatov.phonebook.models.User;
import ru.bulatov.phonebook.repositories.ContactRepository;
import ru.bulatov.phonebook.repositories.PhoneBookRepository;

import java.util.List;

@Service
public class PhoneBookService {

    @Autowired
    private PhoneBookRepository phoneBookRepository;
    @Autowired
    private ContactService contactService;

    public void addContact(User user, Contact contact){
        contactService.create(contact);
        phoneBookRepository.save(new PhoneBook(user.getId(), contactService.find(contact.getPhoneNumber()).getId()));
    }

    public Contact readContact(User user, int contactId) {
        return phoneBookRepository.getOne(contactId));
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
