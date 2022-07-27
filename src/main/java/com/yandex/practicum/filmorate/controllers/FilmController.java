package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.service.FilmService;
import com.yandex.practicum.filmorate.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        log.debug("Films quantity: {}", filmService.findAll().size());
        return filmService.findAll();
    }

    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film) {
        log.debug("New film added with id={}", film.getId());
        return filmService.addFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping(value = "/films/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmService.getFilm(id);
    }

    @PutMapping(value = "/films/{filmId}/like/{userId}")
    public void addLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping(value = "/films/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getMostPopularFilms(count);
    }
}
