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
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        log.debug("Users quantity: {}", userService.findAll().size());
        return userService.findAll();
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping(value = "/users/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @PutMapping(value = "/users/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping(value = "/users/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        userService.removeFriend(userId, friendId);
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        return userService.getUserFriends(id);
    }

    @GetMapping(value = "/users/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriendsWitOtherUser(@PathVariable Integer userId, @PathVariable Integer otherId) {
        return userService.getCommonFriendsWitOtherUser(userId, otherId);
    }
}


