package ru.bulatov.phonebook.models;

import java.util.*;

public class PhoneBook {

    private final Map<Integer, Contact> CONTACTS = new HashMap<>();
    private int CONTACTS_COUNT;

    public List<Contact> getAllContacts() {
        return new ArrayList<>(CONTACTS.values());
    }

    public void addContact(Contact newContact) {
        if (CONTACTS.values().stream().noneMatch(contact -> contact.equals(newContact))) {
            int contactId = ++CONTACTS_COUNT;
            newContact.setId(contactId);
            CONTACTS.put(contactId, newContact);
        }
    }

    public boolean updateContact(Contact updatedContact) {
        if (CONTACTS.containsKey(updatedContact.getId())) {
            Contact contact = CONTACTS.get(updatedContact.getId());
            contact.setName(updatedContact.getName());
            contact.setPhoneNumber(updatedContact.getPhoneNumber());
            return true;
        }
        else return false;
    }

    public Contact getContact(int id) {
        return CONTACTS.get(id);
    }

    public Contact getContact(String phoneNumber){
        Contact result = null;
        String findPhoneNumber = phoneNumber.replaceAll("\\D", "");
        for(Contact contact : CONTACTS.values()) {
            String thisContactNumber = contact.getPhoneNumber().replaceAll("\\D", "");
            if(findPhoneNumber.equals(thisContactNumber)){
                result = contact;
                break;
            }
        }
        return result;
    }

    public boolean deleteContact(int contactId) {
        return CONTACTS.remove(contactId) != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneBook phoneBook = (PhoneBook) o;

        if (CONTACTS_COUNT != phoneBook.CONTACTS_COUNT) return false;
        return CONTACTS.equals(phoneBook.CONTACTS);
    }

    @Override
    public int hashCode() {
        int result = CONTACTS.hashCode();
        result = 31 * result + CONTACTS_COUNT;
        return result;
    }
}
