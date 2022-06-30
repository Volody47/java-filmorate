package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

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
    public Film createUser(@RequestBody Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateUser(@RequestBody Film film) {
        int id = film.getId();
        films.put(id, film);
        return film;
    }
}
