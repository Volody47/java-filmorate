package com.yandex.practicum.filmorate.service;

import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {

    @Autowired
    InMemoryFilmStorage inMemoryFilmStorage;


    public List<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    public Film addFilm(Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }
}
