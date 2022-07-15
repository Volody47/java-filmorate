package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<User> findAll() {
        log.debug("Users quantity: {}", userService.findAll().size());
        return userService.findAll();
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) {
        log.debug("New user created with id={}", user.getId());
        return userService.createUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        log.debug("User with id={} updated", user.getId());
        return userService.updateUser(user);
    }

    @GetMapping(value = "/users/{id}")
    public User getUser(@PathVariable int id) {

        //log.debug("User with id={} updated", user.getId());
        return userService.getUser(id);
    }
}


