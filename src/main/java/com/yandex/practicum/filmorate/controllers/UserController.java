package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();
    private Integer identificator = 0;

    /**
     * Generate Id
     * @return
     */
    public int generateId() {
        return ++identificator;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        ArrayList<User> listOfUsers = new ArrayList<>();
        for (Integer user : users.keySet()) {
            listOfUsers.add(users.get(user));
        }
        return listOfUsers;
        //log.debug("Текущее количество постов: {}", posts.size());
    }


    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        int id = user.getId();
        users.put(id, user);
        return user;
    }
}


