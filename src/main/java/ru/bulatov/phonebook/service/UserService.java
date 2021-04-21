package ru.bulatov.phonebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bulatov.phonebook.models.User;
import ru.bulatov.phonebook.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void create(User user){
        userRepository.save(user);
    }

    public List<User> readAll() {
        return userRepository.findAll();
    }

    public User read(int id) {
        return userRepository.getOne(id);
    }

    public boolean update(int id, User updatedUser) {
        if (userRepository.existsById(id)) {
            updatedUser.setId(id);
            userRepository.save(updatedUser);
            return true;
        }
        return  false;
    }

    public boolean delete(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return  false;
    }

    public User find(String nameOrPartOfName) {
        User foundUser = null;
            for (User user: userRepository.findAll()){
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
        userRepository.deleteAll();
    }

}
