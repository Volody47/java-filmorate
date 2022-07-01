package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.exceptions.InvalidBirthdayException;
import com.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import com.yandex.practicum.filmorate.exceptions.InvalidLoginException;
import com.yandex.practicum.filmorate.exceptions.UnknownUserException;
import com.yandex.practicum.filmorate.model.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.yandex.practicum.filmorate.utils.Validator.validateUser;

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
        log.debug("Users quantity: {}", listOfUsers.size());
        return listOfUsers;

    }


    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) {
        validateUser(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.debug("New user created with id={}", user.getId());
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        for (Integer userId : users.keySet()) {
            if (userId.equals(user.getId())) {
                validateUser(user);
                int id = user.getId();
                users.put(id, user);
                log.debug("User with id={} updated", user.getId());
                return user;
            }
        }
        log.error("Unknown user.");
        throw new UnknownUserException("Unknown user.");
    }
}


