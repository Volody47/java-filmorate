package com.yandex.practicum.filmorate.utils;

import com.yandex.practicum.filmorate.exceptions.*;
import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;

@Slf4j
public class Validator {

    public static void validateUser(@RequestBody User user) {
        if(user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Email: '{}' can't be empty and should contains @", user.getEmail());
            throw new InvalidEmailException("Email can't be empty and should contains @.");
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Login field: '{}' can't be empty or contain space.", user.getLogin());
            throw new InvalidLoginException("Login can't be empty or contain space.");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Birthday: '{}' can't be in the future", user.getBirthday());
            throw new InvalidBirthdayException("Birthday can't be in the future.");
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public static void validateFilm(@RequestBody Film film) {
        if(film.getName() == null || film.getName().isBlank()) {
            log.error("Film name: '{}' can't be empty.", film.getName());
            throw new InvalidFilmNameException("Film name can't be empty.");
        } else if (film.getDescription().length() > 200) {
            log.error("Description field accept max 200 characters.");
            throw new DescriptionLengthException("Description field accept max 200 characters.");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895,12, 28))) {
            log.error("Release date: '{}' can't be before 1895-12-28.", film.getReleaseDate());
            throw new InvalidReleaseDateException("Release can't be before 1895-12-28.");
        } else if (film.getDuration() < 0) {
            log.error("Duration value: '{}' should be more then 0.", film.getDuration());
            throw new InvalidDurationException("Duration should be positive value.");
        }
    }
}
