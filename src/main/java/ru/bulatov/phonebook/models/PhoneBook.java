package ru.bulatov.phonebook.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoneBook {

    private final Map<Integer, Contact> contacts = new HashMap<>();
    private int CONTACTS_COUNT;

    public List<Contact> getAllContacts() {
        return new ArrayList<>(contacts.values());
    }

    public void addContact(Contact newContact) {
        if (contacts.values().stream().noneMatch(contact -> contact.equals(newContact))) {
            int contactId = ++CONTACTS_COUNT;
            newContact.setId(contactId);
            contacts.put(contactId, newContact);
        }
    }

    public boolean updateContact(Contact updatedContact) {
        if (contacts.containsKey(updatedContact.getId())) {
            Contact contact = contacts.get(updatedContact.getId());
            contact.setName(updatedContact.getName());
            contact.setPhoneNumber(updatedContact.getPhoneNumber());
            return true;
        }
        else return false;
    }

    public Contact getContact(int id) {
        return contacts.get(id);
    }

    public Contact getContact(String phoneNumber){
        return contacts.values().stream()
                .filter(contact -> contact.getPhoneNumber().equals(phoneNumber))
                .findAny().orElse(null);
    }

    public boolean deleteContact(int contactId) {
        return contacts.remove(contactId) != null;
    }

}
