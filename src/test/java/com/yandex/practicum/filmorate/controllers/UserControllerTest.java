package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.exceptions.InvalidBirthdayException;
import com.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import com.yandex.practicum.filmorate.exceptions.InvalidLoginException;
import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.yandex.practicum.filmorate.utils.Validator.validateUser;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void validateEmail() {
        User user = new User();
        user.setEmail("");
        assertThrows(InvalidEmailException.class, () -> validateUser(user) );
    }

    @Test
    void validateLogin() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore ullamco");
        assertThrows(InvalidLoginException.class, () -> validateUser(user) );
    }

    @Test
    void validateBirthday() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore");
        user.setBirthday(LocalDate.MAX);
        assertThrows(InvalidBirthdayException.class, () -> validateUser(user) );
    }

}