package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.Genre;
import com.yandex.practicum.filmorate.model.Mpa;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.storage.FilmDbStorageImpl;
import com.yandex.practicum.filmorate.storage.UserDbStorageImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IntegrationFilmControllerTest {

    private final FilmDbStorageImpl filmStorage;
    private final UserDbStorageImpl userStorage;

    @BeforeEach
    public void setUp() {
        Set<Genre> genres = new HashSet<>();
        Mpa mpa = new Mpa(1, "G");
        Genre genre = new Genre(1, "Комедия");
        genres.add(genre);
        Film film = new Film(0, "labore nulla", "Duis in consequat esse", LocalDate.now().minusYears(5), 100, mpa, genres);
        filmStorage.addFilm(film);
    }

    @Test
    public void shouldFindAllIntegrTest() {
        List<Film> allFilms = filmStorage.findAll();

        assertNotNull(allFilms);
        assertTrue(allFilms.size() > 0);
    }

    @Test
    public void shouldAddFilmIntegrTest() {
        Set<Genre> genres = new HashSet<>();
        Mpa mpa = new Mpa(2, "PG");
        Genre genre = new Genre(2, "Драма");
        genres.add(genre);
        Film film = new Film(0, "labore nulla", "Duis in consequat esse", LocalDate.now().minusYears(5), 100, mpa, genres);
        filmStorage.addFilm(film);

        assertEquals("PG", film.getMpa().getName());
    }

    @Test
    public void shouldUpdateFilmIntegrTest() {
        Set<Genre> genres = new HashSet<>();
        Mpa mpa = new Mpa(2, "PG");
        Genre genre = new Genre(2, "Драма");
        genres.add(genre);
        Film film = new Film(1, "updated_labore nulla", "Duis in consequat esse", LocalDate.now().minusYears(5), 100, mpa, genres);
        Film updated_film = filmStorage.updateFilm(film);

        assertEquals(1, updated_film.getId());
        assertEquals("updated_labore nulla", updated_film.getName());
    }

    @Test
    public void shouldGetFilmIntegrTest() {

        Film film = filmStorage.getFilm(2);

        assertEquals(2, film.getId());
        assertEquals("labore nulla", film.getName());
    }

    @Test
    public void shouldAddLikeIntegrTest() {
        User user = new User(0, "mail@mail.ru", "user_login",
                "user_with_friend_name", LocalDate.now());
        userStorage.createUser(user);
        Set<Genre> genres = new HashSet<>();
        Mpa mpa = new Mpa(2, "PG");
        Genre genre = new Genre(2, "Драма");
        genres.add(genre);
        Film film = new Film(0, "labore nulla", "Duis in consequat esse", LocalDate.now().minusYears(5), 100, mpa, genres);
        filmStorage.addFilm(film);

        filmStorage.addLike(film, user);

        List<Film> mostPopularFilms = filmStorage.getMostPopularFilms(1);

        assertNotNull(mostPopularFilms);
        assertEquals("labore nulla", mostPopularFilms.get(0).getName());
    }

    @Test
    public void shouldRemoveLikeIntegrTest() {
        User user = new User(0, "mail@mail.ru", "user_login",
                "user_with_friend_name", LocalDate.now());
        userStorage.createUser(user);
        Set<Genre> genres = new HashSet<>();
        Mpa mpa = new Mpa(2, "PG");
        Genre genre = new Genre(2, "Драма");
        genres.add(genre);
        Film film = new Film(0, "liked_film", "Duis in consequat esse", LocalDate.now().minusYears(5), 100, mpa, genres);
        filmStorage.addFilm(film);

        filmStorage.addLike(film, user);
        filmStorage.removeLike(film, user);

        List<Film> mostPopularFilms = filmStorage.getMostPopularFilms(1);

        assertNotNull(mostPopularFilms);
        assertNotEquals("liked_film", mostPopularFilms.get(0).getMpa().getName());
    }

    @Test
    public void shouldGetMostPopularFilmsIntegrTest() {
        List<Film> mostPopularFilms = filmStorage.getMostPopularFilms(1);

        assertNotNull(mostPopularFilms);
        assertTrue(mostPopularFilms.size() > 0);
    }

    @Test
    public void shouldFindAllGenresIntegrTest() {
        List<Genre> allGenres = filmStorage.findAllGenres();

        assertNotNull(allGenres);
        assertTrue(allGenres.size() == 6);
    }

    @Test
    public void shouldGetGenreIntegrTest() {
        Genre genre = filmStorage.getGenre(1);

        assertNotNull(genre);
        assertEquals("Комедия", genre.getName());
    }

    @Test
    public void shouldFindAllMpaIntegrTest() {
        List<Mpa> allMpa = filmStorage.findAllMpa();

        assertNotNull(allMpa);
        assertTrue(allMpa.size() == 5);
    }

    @Test
    public void shouldGetMpaIntegrTest() {
        Mpa mpa = filmStorage.getMpa(1);

        assertNotNull(mpa);
        assertEquals("G", mpa.getName());
    }
}
