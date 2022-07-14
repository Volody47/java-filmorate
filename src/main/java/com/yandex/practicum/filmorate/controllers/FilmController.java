package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
public class FilmController {

    @Autowired
    FilmService filmService;

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
}
