package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.exceptions.InvalidBirthdayException;
import com.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import com.yandex.practicum.filmorate.exceptions.InvalidLoginException;
import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void validateEmail() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("");
        assertThrows(InvalidEmailException.class, () -> userController.validate(user) );
    }

    @Test
    void validateLogin() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore ullamco");
        assertThrows(InvalidLoginException.class, () -> userController.validate(user) );
    }

    @Test
    void validateBirthday() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore");
        user.setBirthday(LocalDate.MAX);
        assertThrows(InvalidBirthdayException.class, () -> userController.validate(user) );
    }

}