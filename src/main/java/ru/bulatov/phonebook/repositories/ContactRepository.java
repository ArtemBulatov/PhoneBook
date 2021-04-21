package ru.bulatov.phonebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bulatov.phonebook.models.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
}
