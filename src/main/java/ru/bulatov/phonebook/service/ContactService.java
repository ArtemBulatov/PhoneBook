package ru.bulatov.phonebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bulatov.phonebook.models.Contact;
import ru.bulatov.phonebook.repositories.ContactsRepository;

import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactsRepository contactsRepository;

    public void create(Contact contact){
        contactsRepository.save(contact);
    }

    public List<Contact> readAll() {
        return contactsRepository.findAll();
    }

    public Contact read(int id) {
        return contactsRepository.getOne(id);
    }

    public boolean update(int id, Contact updatedContact) {
        if (contactsRepository.existsById(id)) {
            updatedContact.setId(id);
            contactsRepository.save(updatedContact);
            return true;
        }
        return  false;
    }

    public boolean delete(int id) {
        if (contactsRepository.existsById(id)) {
            contactsRepository.deleteById(id);
            return true;
        }
        return  false;
    }

    public Contact find(String phoneNumber) {
        Contact result = null;
        String findPhoneNumber = phoneNumber.replaceAll("\\D", "");
        for(Contact contact : contactsRepository.findAll()) {
            String thisContactNumber = contact.getPhoneNumber().replaceAll("\\D", "");
            if(findPhoneNumber.equals(thisContactNumber)){
                result = contact;
                break;
            }
        }
        return result;
    }

    public void deleteAllContacts() {
        contactsRepository.deleteAll();
    }

}
