package ru.bulatov.phonebook.service;

import ru.bulatov.phonebook.models.User;

import java.util.List;

public interface UserService {

    /**
     * Создает нового пользователя
     * @param user - пользователь для создания
     */
    void create(User user);

    /**
     * Возвращает список всех имеющихся пользователей
     * @return список пользователей
     */
    List<User> readAll();

    /**
     * Возвращает пользователя по его ID
     * @param id - ID пользователя
     * @return - объект пользователя с заданным ID
     */
    User read(int id);

    /**
     * Обновляет пользователя с заданным ID,
     * в соответствии с переданным пользователем
     * @param user - пользователь в соответсвии с которым нужно обновить данные
     * @param id - id пользователя которого нужно обновить
     * @return - true если данные были обновлены, иначе false
     */
    boolean update(int id, User user);

    /**
     * Удаляет пользователя с заданным ID
     * @param id - id пользователя, которого нужно удалить
     * @return - true если пользователя был удален, иначе false
     */
    boolean delete(int id);
}
