package ru.bulatov.phonebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bulatov.phonebook.models.Contact;
import ru.bulatov.phonebook.models.User;

import java.util.List;

@Service
public class PhoneBookService {

    @Autowired
    private ContactService contactService;

    public void addContact(User user, Contact contact){
        contact.setUserId(user.getId());
        contactService.create(contact);
    }

    public Contact readContact(User user, int contactId) {
        Contact foundContact = null;
        if(contactService.read(contactId).getUserId() == user.getId()){
            foundContact = contactService.read(contactId);
        }
        return foundContact;
    }

    public Contact findContact(User user, String phoneNumber){
        Contact foundContact = null;
        Contact contact = contactService.find(phoneNumber);
        if (contact.getUserId() == user.getId()){
            foundContact = contact;
        }
        return foundContact;
    }

    public List<Contact> getAllContacts(User user){
        return user.getContacts();
    }

    public boolean updateContact(User user, Contact contact){
        return contactService.update(user.getId(), contact);
    }

    public boolean deleteContact(User user, int contactId) {
        if(user.getContacts().contains(contactService.read(contactId))) {
            return contactService.delete(contactId);
        }
        else
            return false;
    }

    public void cleanPhoneBook(){
        contactService.deleteAllContacts();
    }

}
