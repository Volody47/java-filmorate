package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.exceptions.*;
import com.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.lang.module.InvalidModuleDescriptorException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.yandex.practicum.filmorate.utils.Validator.validateFilm;

@RestController
@Slf4j
public class FilmController {
    private HashMap<Integer, Film> films = new HashMap<>();
    private Integer identificator = 0;

    /**
     * Generate Id
     * @return
     */
    public int generateId() {
        return ++identificator;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        ArrayList<Film> listOfFilms = new ArrayList<>();
        for (Integer film : films.keySet()) {
            listOfFilms.add(films.get(film));
        }
        log.debug("Films quantity: {}", listOfFilms.size());
        return listOfFilms;
    }


    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film) {
        validateFilm(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        log.debug("New film added with id={}", film.getId());
        return film;
    }


    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) {
        for (Integer filmId : films.keySet()) {
            if (filmId.equals(film.getId())) {
                validateFilm(film);
                int id = film.getId();
                films.put(id, film);
                log.debug("Film with id={} updated", film.getId());
                return film;
            }
        }
        throw new UnknownFilmException("Unknown film.");
    }
}
