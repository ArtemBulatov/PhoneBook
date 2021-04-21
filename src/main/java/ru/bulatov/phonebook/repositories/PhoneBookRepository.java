package ru.bulatov.phonebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bulatov.phonebook.models.PhoneBook;

public interface PhoneBookRepository extends JpaRepository<PhoneBook, Integer> {
}
