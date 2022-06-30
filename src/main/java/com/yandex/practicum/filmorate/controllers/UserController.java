package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.exceptions.InvalidBirthdayException;
import com.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import com.yandex.practicum.filmorate.exceptions.InvalidLoginException;
import com.yandex.practicum.filmorate.exceptions.UnknownUserException;
import com.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
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
        if(user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new InvalidEmailException("Email can't be empty and should contains @.");
            //log.error("Email: %s" + "can't be empty and should contains @", user.getEmail());
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new InvalidLoginException("Login can't be empty or contain space.");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new InvalidBirthdayException("Birthday can't be in the future.");
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        log.debug("New user created");
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        for (Integer userId : users.keySet()) {
            if (userId.equals(user.getId())) {
                if(user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                    throw new InvalidEmailException("Email can't be empty and should contains @.");
                } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
                    throw new InvalidLoginException("Login can't be empty or contain space.");
                } else if (user.getBirthday().isAfter(LocalDate.now())) {
                    throw new InvalidBirthdayException("Birthday can't be in the future.");
                } else if (user.getName() == null || user.getName().isBlank()) {
                    user.setName(user.getLogin());
                }
                int id = user.getId();
                users.put(id, user);
                return user;
            }
        }
        throw new UnknownUserException("Unknown user.");
    }
}


