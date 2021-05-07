package ru.bulatov.phonebook.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "contacts")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Contact {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name of contact should not be empty")
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
    @Pattern(regexp = "^[A-ZА-Я][a-zа-яё]*(\\s(([a-zа-яё]{1,3})|(([a-zа-яё]+')?([A-ZА-Я][a-zа-яё]+-)?[A-ZА-Яё][a-zа-яё]*)))*$",
            message = "Bad formed person name: ${validatedValue}")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "PhoneNumber should not be empty")
    @Pattern(regexp = "^(\\+)?(\\(\\d{2,3}\\) ?\\d|\\d)(([ \\-]?\\d)|( ?\\(\\d{2,3}\\) ?)){5,12}\\d$",
            message = "Bad formed phoneNumber: ${validatedValue}")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "user_id")
    private int userId;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (!Objects.equals(name, contact.name)) return false;
        return Objects.equals(phoneNumber, contact.phoneNumber);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }

}
