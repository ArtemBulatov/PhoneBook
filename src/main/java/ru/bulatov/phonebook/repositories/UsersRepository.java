package ru.bulatov.phonebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bulatov.phonebook.models.User;

public interface UsersRepository extends JpaRepository<User, Integer> {
}
