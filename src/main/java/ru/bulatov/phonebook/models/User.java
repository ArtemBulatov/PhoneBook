package ru.bulatov.phonebook.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class User {

    private int id;

    @NotEmpty(message = "Name of user should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String name;

    private final PhoneBook phoneBook = new PhoneBook();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PhoneBook getPhoneBook() {
        return phoneBook;
    }

}
