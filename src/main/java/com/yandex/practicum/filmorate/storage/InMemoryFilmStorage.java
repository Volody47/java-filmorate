package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.service.FilmService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
        throw new FilmNotFoundException("Unknown film.");
    }

    @Override
    public void remove(Film film) {
        films.remove(film.getId());
    }

    public Film getFilm(int id) {
        Film film = null;
        for (Integer filmId : films.keySet()) {
            if (filmId == id) {
                film = films.get(id);
            }
        }
        return film;
    }

    public void addLike(Film film, Film user) {
        film.getUserWhoLikedIds().add(user.getId());
    }

    public void removeLike(Film film, Film user) {
        film.getUserWhoLikedIds().remove(user.getId());
    }

    public List<Film> getMostPopularFilms(Integer count) {
        int size = 0;
        return films.values()
                .stream()
                .sorted((p0, p1) -> compare(p0, p1))
                //.sorted(Comparator.comparing(Film::getName))
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film p0, Film p1) {
        int result = Integer.compare(p1.getUserWhoLikedIds().size(), p0.getUserWhoLikedIds().size()); //прямой порядок сортировки
        //int result = p0.getUserWhoLikedIds().size().compareTo(p1.getUserWhoLikedIds().size()); //прямой порядок сортировки
        return result;
    }
}
