package com.yandex.practicum.filmorate.service;

import com.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.storage.InMemoryUserStorageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final InMemoryUserStorageImpl userStorage;

    @Autowired
    public UserService(InMemoryUserStorageImpl userStorage) {
        this.userStorage = userStorage;
    }

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

    public List<User> getUserFriends(Integer userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException("User with id=" + userId + " not found.");
        }
        return userStorage.getUserFriends(user);
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null) {
            throw new UserNotFoundException("User with id=" + userId + " not found.");
        } else if (friend == null) {
            throw new UserNotFoundException("Friend with id=" + friendId + " not found.");
        }
        userStorage.addFriend(user, friend);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null) {
            throw new UserNotFoundException("User with id=" + userId + " not found.");
        } else if (friend == null) {
            throw new UserNotFoundException("Friend with id=" + friendId + " not found.");
        }
        userStorage.removeFriend(user, friend);
    }

    public List<User> getCommonFriendsWitOtherUser(Integer userId, Integer otherId) {
        User user = userStorage.getUser(userId);
        User otherUser = userStorage.getUser(otherId);
        if (user == null) {
            throw new UserNotFoundException("User with id=" + userId + " not found.");
        } else if (otherUser == null) {
            throw new UserNotFoundException("OtherUser with id=" + otherId + " not found.");
        }
        return userStorage.getCommonFriendsWitOtherUser(user, otherUser);
    }
}
