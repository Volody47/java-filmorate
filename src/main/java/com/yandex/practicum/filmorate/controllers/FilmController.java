package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.exceptions.*;
import com.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.lang.module.InvalidModuleDescriptorException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
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
        return listOfFilms;
        //log.debug("Текущее количество постов: {}", posts.size());
    }


    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film) {
        if(film.getName() == null || film.getName().isBlank()) {
            throw new InvalidFilmNameException("Film name can't be empty.");
        } else if (film.getDescription().length() > 200) {
            throw new DescriptionLengthException("Description field accept max 200 characters.");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895,12, 28))) {
            throw new InvalidReleaseDateException("Release can't be before 12/28/1895.");
        } else if (film.getDuration() < 0) {
            throw new InvalidDurationException("Duration should be positive value.");
        }
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) {
        for (Integer filmId : films.keySet()) {
            if (filmId.equals(film.getId())) {
                if(film.getName() == null || film.getName().isBlank()) {
                    throw new InvalidFilmNameException("Film name can't be empty.");
                } else if (film.getDescription().length() > 200) {
                    throw new DescriptionLengthException("Description field accept max 200 characters.");
                } else if (film.getReleaseDate().isBefore(LocalDate.of(1895,12, 28))) {
                    throw new InvalidReleaseDateException("Release can't be before 12/28/1895.");
                } else if (film.getDuration() < 0) {
                    throw new InvalidDurationException("Duration should be positive value.");
                }
                int id = film.getId();
                films.put(id, film);
                return film;
            }
        }
        throw new UnknownFilmException("Unknown film.");
    }
}
