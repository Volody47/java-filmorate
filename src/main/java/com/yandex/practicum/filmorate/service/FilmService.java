package com.yandex.practicum.filmorate.service;

import com.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import com.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import com.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import com.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.Genre;
import com.yandex.practicum.filmorate.model.Mpa;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.storage.FilmDbStorageImpl;
import com.yandex.practicum.filmorate.storage.InMemoryFilmStorageImpl;
import com.yandex.practicum.filmorate.storage.UserDbStorageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmService {
    private final FilmDbStorageImpl filmStorage;
    private final UserDbStorageImpl userStorage;

    @Autowired
    public FilmService(FilmDbStorageImpl filmStorage, UserDbStorageImpl userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        Film updatedFilm = filmStorage.updateFilm(film);
        if (updatedFilm == null) {
            throw new FilmNotFoundException("Film with id=" + film.getId() + " not found.");
        }
        return updatedFilm;
    }

    public Film getFilm(int id) {
        Film film = filmStorage.getFilm(id);
        if (film == null) {
            throw new FilmNotFoundException("Film with id=" + id + " not found.");
        }
        return film;
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        if (film == null) {
            throw new FilmNotFoundException("Film with id=" + filmId + " not found.");
        } else if (user == null) {
            throw new UserNotFoundException("User with id=" + userId + " not found.");
        }
        filmStorage.addLike(film, user);
    }


    public void removeLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilm(filmId);
        Film user = filmStorage.getFilm(userId);
        if (film == null) {
            throw new FilmNotFoundException("Film with id=" + filmId + " not found.");
        } else if (user == null) {
            throw new UserNotFoundException("User with id=" + userId + " not found.");
        }
        filmStorage.removeLike(film, user);
    }

    public List<Film> getMostPopularFilms(Integer count) {
        return filmStorage.getMostPopularFilms(count);
    }

    public List<Genre> findAllGenres() {
        return filmStorage.findAllGenres();
    }

    public Genre getGenre(int id) {
        Genre genre = filmStorage.getGenre(id);
        if (genre == null) {
            throw new GenreNotFoundException("Genre with id=" + id + " not found.");
        }
        return genre;
    }

    public List<Mpa> findAllMpa() {
        return filmStorage.findAllMpa();
    }

    public Mpa getMpa(int id) {
        Mpa mpa = filmStorage.getMpa(id);
        if (mpa == null) {
            throw new MpaNotFoundException("Mpa with id=" + id + " not found.");
        }
        return mpa;
    }
}
