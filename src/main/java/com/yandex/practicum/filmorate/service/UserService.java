package com.yandex.practicum.filmorate.service;

import com.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    InMemoryUserStorage userStorage;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        User updatedUser = userStorage.updateUser(user);
        if (updatedUser == null) {
            throw new UserNotFoundException("User with id=" + user.getId() + " not found.");
        }
        return updatedUser;
    }

    public User getUser(int id) {
        User user = userStorage.getUser(id);
        if (user == null) {
            throw new UserNotFoundException("User with id=" + id + " not found.");
        }
        return user;
    }
}
