package ru.bulatov.phonebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bulatov.phonebook.models.User;
import ru.bulatov.phonebook.repositories.UsersRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    public void create(User user){
        usersRepository.save(user);
    }

    public List<User> readAll() {
        return usersRepository.findAll();
    }

    public User read(int id) {
        return usersRepository.getOne(id);
    }

    public boolean update(int id, User updatedUser) {
        if (usersRepository.existsById(id)) {
            updatedUser.setId(id);
            usersRepository.save(updatedUser);
            return true;
        }
        return  false;
    }

    public boolean delete(int id) {
        if (usersRepository.existsById(id)) {
            usersRepository.deleteById(id);
            return true;
        }
        return  false;
    }

    public User find(String nameOrPartOfName) {
        User foundUser = null;
            for (User user: usersRepository.findAll()){
                if(user.getName().equals(nameOrPartOfName)) {
                    foundUser = user;
                }
                else {
                    String[] partsOfName = user.getName().split("\\s|-");
                    for (String s : partsOfName){
                        if (s.equals(nameOrPartOfName)) {
                            foundUser = user;
                            break;
                        }
                    }
                }
                if(foundUser != null)
                    break;
            }

        return foundUser;
    }

    public void deleteAllUsers() {
        usersRepository.deleteAll();
    }

}
