package com.yandex.practicum.filmorate.service;

import com.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import com.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Film getFilm(int id) {
        Film film = inMemoryFilmStorage.getFilm(id);
        if (film == null) {
            throw new FilmNotFoundException("Film with id=" + id + " not found.");
        }
        return film;
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = inMemoryFilmStorage.getFilm(filmId);
        Film user = inMemoryFilmStorage.getFilm(userId);
        if (film == null) {
            throw new FilmNotFoundException("Film with id=" + filmId + " not found.");
        } else if (user == null) {
            throw new UserNotFoundException("User with id=" + userId + " not found.");
        }
        inMemoryFilmStorage.addLike(film, user);
    }


    public void removeLike(Integer filmId, Integer userId) {
        Film film = inMemoryFilmStorage.getFilm(filmId);
        Film user = inMemoryFilmStorage.getFilm(userId);
        if (film == null) {
            throw new FilmNotFoundException("Film with id=" + filmId + " not found.");
        } else if (user == null) {
            throw new UserNotFoundException("User with id=" + userId + " not found.");
        }
        inMemoryFilmStorage.removeLike(film, user);
    }

    public List<Film> getMostPopularFilms(Integer count) {
        return inMemoryFilmStorage.getMostPopularFilms(count);
    }
}
