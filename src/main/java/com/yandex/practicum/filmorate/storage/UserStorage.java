package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.model.User;


public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    void remove(User user);
}
