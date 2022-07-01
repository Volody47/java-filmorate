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
        validate(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        log.debug("New film added with id={}", film.getId());
        return film;
    }

    public void validate(@RequestBody Film film) {
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

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) {
        for (Integer filmId : films.keySet()) {
            if (filmId.equals(film.getId())) {
                validate(film);
                int id = film.getId();
                films.put(id, film);
                log.debug("Film with id={} updated", film.getId());
                return film;
            }
        }
        throw new UnknownFilmException("Unknown film.");
    }
}
