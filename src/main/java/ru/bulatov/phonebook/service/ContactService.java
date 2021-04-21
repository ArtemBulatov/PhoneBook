package ru.bulatov.phonebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bulatov.phonebook.models.Contact;
import ru.bulatov.phonebook.repositories.ContactRepository;

import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public void create(Contact contact){
        contactRepository.save(contact);
    }

    public List<Contact> readAll() {
        return contactRepository.findAll();
    }

    public Contact read(int id) {
        return contactRepository.getOne(id);
    }

    public boolean update(int id, Contact updatedContact) {
        if (contactRepository.existsById(id)) {
            updatedContact.setId(id);
            contactRepository.save(updatedContact);
            return true;
        }
        return  false;
    }

    public boolean delete(int id) {
        if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id);
            return true;
        }
        return  false;
    }

    public Contact find(String phoneNumber) {
        Contact result = null;
        String findPhoneNumber = phoneNumber.replaceAll("\\D", "");
        for(Contact contact : contactRepository.findAll()) {
            String thisContactNumber = contact.getPhoneNumber().replaceAll("\\D", "");
            if(findPhoneNumber.equals(thisContactNumber)){
                result = contact;
                break;
            }
        }
        return result;
    }

}
