package ru.bulatov.phonebook.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.*;
@Entity
@Table(name = "phoneBook")
public class PhoneBook {

    @Column(name = "userId")
    private int userId;

    @Id
    @Column(name = "contactId")
    private int contactId;

    public PhoneBook() {
    }

    public PhoneBook(int userId, int contactId) {
        this.userId = userId;
        this.contactId = contactId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}
