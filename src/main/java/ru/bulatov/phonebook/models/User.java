package ru.bulatov.phonebook.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class User {

    private int id;

    @NotEmpty(message = "Name of user should not be empty")
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Z][a-z]*(\\s(([a-z]{1,3})|(([a-z]+')?[A-Z][a-z]*)))*$",
            message = "Bad formed person name: ${validatedValue}")
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
