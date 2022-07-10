package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.exceptions.DescriptionLengthException;
import com.yandex.practicum.filmorate.exceptions.InvalidDurationException;
import com.yandex.practicum.filmorate.exceptions.InvalidFilmNameException;
import com.yandex.practicum.filmorate.exceptions.InvalidReleaseDateException;
import com.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.yandex.practicum.filmorate.utils.Validator.validateFilm;
import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Test
    void validateNameField() {
        Film film = new Film();
        film.setName("");
        assertThrows(InvalidFilmNameException.class, () -> validateFilm(film) );
    }

    @Test
    void validateDescriptionLength() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.");
        assertThrows(DescriptionLengthException.class, () -> validateFilm(film) );
    }

    @Test
    void validateReleaseDate() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("TestDescription");
        film.setReleaseDate(LocalDate.MIN);
        assertThrows(InvalidReleaseDateException.class, () -> validateFilm(film) );
    }

    @Test
    void validateDurationField() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("TestDescription");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(-100);
        assertThrows(InvalidDurationException.class, () -> validateFilm(film) );
    }
}