package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void remove(Film film);
}
