package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.exceptions.UnknownFilmException;
import com.yandex.practicum.filmorate.model.Film;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.yandex.practicum.filmorate.utils.Validator.validateFilm;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Integer, Film> films = new HashMap<>();
    private Integer identificator = 0;

    /**
     * Generate Id
     * @return
     */
    public int generateId() {
        return ++identificator;
    }

    public List<Film> findAll() {
        ArrayList<Film> listOfFilms = new ArrayList<>();
        for (Integer film : films.keySet()) {
            listOfFilms.add(films.get(film));
        }
        //log.debug("Films quantity: {}", listOfFilms.size());
        return listOfFilms;
    }

    @Override
    public Film addFilm(Film film) {
        validateFilm(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        //log.debug("New film added with id={}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        for (Integer filmId : films.keySet()) {
            if (filmId.equals(film.getId())) {
                validateFilm(film);
                int id = film.getId();
                films.put(id, film);
                //log.debug("Film with id={} updated", film.getId());
                return film;
            }
        }
        throw new UnknownFilmException("Unknown film.");
    }

    @Override
    public void remove(Film film) {
        films.remove(film.getId());
    }
}
