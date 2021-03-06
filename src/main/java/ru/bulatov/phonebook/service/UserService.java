package ru.bulatov.phonebook.service;

import org.springframework.stereotype.Service;
import ru.bulatov.phonebook.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService {

    private final Map<Integer, User> USER_MAP = new HashMap<>();

    private final AtomicInteger USER_ID_HOLDER = new AtomicInteger();

    public void create(User user){
        int userId = USER_ID_HOLDER.incrementAndGet();
        user.setId(userId);
        USER_MAP.put(userId, user);
    }

    public List<User> readAll() {
        return new ArrayList<>(USER_MAP.values());
    }

    public User read(int id) {
        return USER_MAP.get(id);
    }

    public boolean update(int id, User updatedUser) {
        if (USER_MAP.containsKey(id)) {
            User userToBeUpdated = USER_MAP.get(id);
            String newName = updatedUser.getName();
            userToBeUpdated.setName(newName);
            return true;
        }
        return  false;
    }

    public boolean delete(int id) {
        return USER_MAP.remove(id) != null;
    }

    public User find(String nameOrPartOfName) {
        User foundUser = null;
            for (User user: USER_MAP.values()){
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
        USER_MAP.clear();
    }

}
