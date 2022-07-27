package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.exceptions.DescriptionLengthException;
import com.yandex.practicum.filmorate.exceptions.InvalidDurationException;
import com.yandex.practicum.filmorate.exceptions.InvalidFilmNameException;
import com.yandex.practicum.filmorate.exceptions.InvalidReleaseDateException;
import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.service.FilmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static com.yandex.practicum.filmorate.utils.Validator.validateFilm;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmControllerTest {
    private MockMvc restMvc;
    private FilmService filmService;

    @BeforeEach
    public void setUp() {
        filmService = mock(FilmService.class);
        FilmController filmController = new FilmController(filmService);
        this.restMvc = MockMvcBuilders.standaloneSetup(filmController).build();
    }

    @Test
    void validateNameField() {
        Film film = new Film();
        film.setName("");
        assertThrows(InvalidFilmNameException.class, () -> validateFilm(film) );
    }

    @Test
    void validateDescriptionLength() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.");
        assertThrows(DescriptionLengthException.class, () -> validateFilm(film) );
    }

    @Test
    void validateReleaseDate() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("TestDescription");
        film.setReleaseDate(LocalDate.MIN);
        assertThrows(InvalidReleaseDateException.class, () -> validateFilm(film) );
    }

    @Test
    void validateDurationField() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("TestDescription");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(-100);
        assertThrows(InvalidDurationException.class, () -> validateFilm(film) );
    }

    @Test
    void shouldFindAllTest() throws Exception {
        Film film = new Film();
        film.setName("labore nulla");
        film.setReleaseDate(LocalDate.now().minusYears(10));
        film.setDescription("Duis in consequat esse");
        film.setDuration(100);

        when(filmService.findAll()).thenReturn(List.of(film));

        restMvc.perform(
                get("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Duis in consequat esse"));
    }

    @Test
    void shouldAddFilmTest() throws Exception {
        restMvc.perform(
                post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"labore nulla\", \"description\": \"Duis in consequat esse\", \"duration\": \"100\", \"releaseDate\": \"1979-04-17\"}")
                        .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(filmService).addFilm(any(Film.class));
    }

    @Test
    void shouldUpdateFilmTest() throws Exception {
        restMvc.perform(
                put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Film Updated\", \"description\": \"Duis in consequat esse\", \"duration\": \"100\", \"releaseDate\": \"1979-04-17\"}")
                        .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(filmService).updateFilm(any(Film.class));
    }

    @Test
    void shouldGetFilmTest() throws Exception {
        int id = 35;
        Film film = new Film();
        film.setId(id);
        film.setName("labore nulla");
        film.setReleaseDate(LocalDate.now().minusYears(10));
        film.setDescription("Duis in consequat esse");
        film.setDuration(100);

        when(filmService.getFilm(id)).thenReturn((film));

        restMvc.perform(
                get("/films/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Duis in consequat esse"));
    }

    @Test
    void shouldAddLikeTest() throws Exception {
        int filmId = 1;
        int userId = 2;
        restMvc.perform(
                put("/films/{filmId}/like/{userId}", filmId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldRemoveLikeTest() throws Exception {
        int filmId = 1;
        int userId = 2;
        restMvc.perform(
                delete("/films/{filmId}/like/{userId}", filmId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetMostPopularFilmsTest() throws Exception {
        int filmId1 = 1;
        int filmId2 = 2;

        Film film1 = new Film();
        film1.setId(filmId1);
        film1.setName("labore nulla");
        film1.setReleaseDate(LocalDate.now().minusYears(10));
        film1.setDescription("Duis in consequat esse");
        film1.setDuration(100);


        when(filmService.getMostPopularFilms(1)).thenReturn(List.of(film1));
        restMvc.perform(
                get("/films/popular?count=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("labore nulla"));
    }
}