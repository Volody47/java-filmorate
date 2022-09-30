package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.Genre;
import com.yandex.practicum.filmorate.model.Mpa;
import com.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.yandex.practicum.filmorate.utils.Validator.validateFilm;

@Repository
@Slf4j
public class FilmDbStorageImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Film addFilm(Film film) {
        validateFilm(film);
        String sqlQuery = "insert into FILMS (FILM_NAME, FILM_DESCRIPTION, FILM_RELEASE_DATE," +
                " FILM_DURATION, MPA_ID) values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            stmt.setInt(4, film.getDuration());
            SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from MPA_RATINGS" +
                    " where MPA_ID = ?", film.getMpa().getId());
            if(mpaRows.next()) {
                String name = mpaRows.getString("NAME");
                int mpa_id = mpaRows.getInt("MPA_ID");
                Mpa mpa = new Mpa(mpa_id, name);
                film.setMpa(mpa);
            } else {
                log.info("Mpa with id= {} haven't found.", film.getMpa().getId());
                return null;
            }
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        Set<Genre> genres = new HashSet<>();
        Set<Genre> genres1 = film.getGenres();
        if (genres1 == null) {
            return film;
        }
        for (Genre genreTest : genres1) {
            int id = genreTest.getId();
            SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from GENRES as g" +
                    " where g.GENRE_ID = ?", id);
            if (genreRows.next()) {
                String sql = "insert into FILMS_GENDER (FILM_ID, GENRE_ID) values (?, ?)";
                int genre_id = genreRows.getInt("GENRE_ID");
                String genre_name = genreRows.getString("GENRE_NAME");
                Genre genre = new Genre(genre_id, genre_name);
                genres.add(genre);
                film.setGenres(genres);
                jdbcTemplate.update(sql
                        , film.getId()
                        , genre_id);
            }
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validateFilm(film);
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from FILMS where FILM_ID = ?", film.getId());
        if (!userRows.next()) {
            log.debug("Film with id={} not found", film.getId());
            return null;
        }
        String sqlQuery = "UPDATE FILMS SET FILM_NAME = ?, FILM_DESCRIPTION = ?, FILM_RELEASE_DATE = ?" +
                ", FILM_DURATION = ?, MPA_ID = ? WHERE FILM_ID = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from MPA_RATINGS" +
                " where MPA_ID = ?", film.getMpa().getId());
        if (mpaRows.next()) {
            String name = mpaRows.getString("NAME");
            int mpa_id = mpaRows.getInt("MPA_ID");
            Mpa mpa = new Mpa(mpa_id, name);
            film.setMpa(mpa);
        } else {
            log.info("Mpa with id= {} not found.", film.getMpa().getId());
        }
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());
         //Delete Genders before update it
        String sqlDelete = "delete from FILMS_GENDER where FILM_ID = ?";
        boolean deleted = jdbcTemplate.update(sqlDelete, film.getId()) > 0;
        if (deleted) {
            log.debug("Genders for Film with id={} removed", film.getId());
        } else {
            log.debug("Genders for Film with id={} not found", film.getId());
        }
        Set<Genre> genres = new HashSet<>();
        Set<Genre> genres1 = film.getGenres();
        if (genres1 == null) {
            return film;
        }
        for (Genre genreTest : genres1) {
            int id = genreTest.getId();
            SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from GENRES as g" +
                    " where g.GENRE_ID = ?", id);
            if (genreRows.next()) {
                String sql = "insert into FILMS_GENDER (FILM_ID, GENRE_ID) values (?, ?)";
                int genre_id = genreRows.getInt("GENRE_ID");
                String genre_name = genreRows.getString("GENRE_NAME");
                Genre genre = new Genre(genre_id, genre_name);
                genres.add(genre);
                film.setGenres(genres);
                jdbcTemplate.update(sql
                        , film.getId()
                        , genre_id);
            }
        }
        log.debug("Film with id={} updated", film.getId());
        return film;
    }

    @Override
    public void remove(Film film) {
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        boolean deleted = jdbcTemplate.update(sqlQuery, film.getId()) > 0;
        if (deleted) {
            log.debug("User with id={} removed", film.getId());
        } else {
            log.debug("User with id={} haven't found", film.getId());
        }
    }

    public List<Film> findAll() {
        List<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILMS");
        if (filmRows.next()) {
            String sqlQuery = "select *" +
                    " from FILMS as f" +
                    " RIGHT JOIN MPA_RATINGS as mp ON f.MPA_ID = mp.MPA_ID";
            films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        }
        return films;
    }


    public Film getFilm(int id) {
        Set<Genre> genres = new HashSet<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from FILMS_GENDER as fg" +
                " RIGHT JOIN GENRES as g ON fg.GENRE_ID = g.GENRE_ID" +
                " where FILM_ID = ?", id);
        while (genreRows.next()) {
            int genre_id = genreRows.getInt("GENRE_ID");
            String genre_name = genreRows.getString("GENRE_NAME");
            Genre genre = new Genre(genre_id, genre_name);
            genres.add(genre);
        }
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select *" +
                " from FILMS as f" +
                " LEFT JOIN MPA_RATINGS as mp ON f.MPA_ID = mp.MPA_ID" +
                " LEFT JOIN FILMS_LIKE as fl ON f.FILM_ID = fl.FILM_ID" +
                " LEFT JOIN FILMS_GENDER as fg ON fl.FILM_ID = fg.FILM_ID" +
                " LEFT JOIN GENRES as g ON fg.GENRE_ID = g.GENRE_ID" +
                " where f.FILM_ID = ?", id);
        if (filmRows.next()) {
            int film_id = filmRows.getInt("FILM_ID");
            String name = filmRows.getString("FILM_NAME");
            String description = filmRows.getString("FILM_DESCRIPTION");
            LocalDate releaseDate = filmRows.getDate("FILM_RELEASE_DATE").toLocalDate();
            int filmDuration = filmRows.getInt("FILM_DURATION");
            Mpa mpa = new Mpa(filmRows.getInt("MPA_ID"), filmRows.getString("NAME"));
            return new Film(film_id, name, description, releaseDate, filmDuration, mpa, genres);
        } else {
            log.info("Film with id= {} not found.", id);
            return null;
        }
    }

    public void addLike(Film film, User user) {
        String sqlQuery = "insert into FILMS_LIKE (USER_ID, FILM_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery
                , user.getId()
                , film.getId());
    }

    public void removeLike(Film film, Film user) {
        String sqlQuery = "delete from FILMS_LIKE where USER_ID = ? and FILM_ID = ?";
        boolean deleted = jdbcTemplate.update(sqlQuery, user.getId(), film.getId()) > 0;
        if (deleted) {
            log.debug("Film with id={} removed", film.getId());
        } else {
            log.debug("Film with id={} not found", film.getId());
        }
    }

    public List<Film> getMostPopularFilms(Integer count) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILMS_LIKE");
        if (!filmRows.next()) {
            String sqlQuery = "select *" +
                    " from FILMS as f" +
                    " LEFT JOIN MPA_RATINGS as mp ON f.MPA_ID = mp.MPA_ID" +
                    " LEFT JOIN FILMS_LIKE as fl ON f.FILM_ID = fl.FILM_ID" +
                    " LEFT JOIN FILMS_GENDER as fg ON fl.FILM_ID = fg.FILM_ID" +
                    " LEFT JOIN GENRES as g ON fg.GENRE_ID = g.GENRE_ID" +
                    " LIMIT " + count;
            List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
            return films;
        }
        String sqlQuery = "select *" +
                " from FILMS as f" +
                " LEFT JOIN MPA_RATINGS as mp ON f.MPA_ID = mp.MPA_ID" +
                " LEFT JOIN FILMS_LIKE as fl ON f.FILM_ID = fl.FILM_ID" +
                " LEFT JOIN FILMS_GENDER as fg ON fl.FILM_ID = fg.FILM_ID" +
                " LEFT JOIN GENRES as g ON fg.GENRE_ID = g.GENRE_ID" +
                " WHERE f.FILM_ID in (select fl.FILM_ID from FILMS_LIKE)" +
                " LIMIT " + count;
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        if (films.size() == 0) {
            return null;
        }
        return films;
    }

    public List<Genre> findAllGenres() {
        String sqlQuery = "select * from GENRES";
        List<Genre> genre = jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
        if (genre.size() == 0) {
            return null;
        }
        return genre;
    }

    public Genre getGenre(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select *" +
                " from GENRES " +
                " where GENRE_ID = ?", id);
        if(filmRows.next()) {
            int genre_id = filmRows.getInt("GENRE_ID");
            String name = filmRows.getString("GENRE_NAME");
            return new Genre(genre_id, name);
        } else {
            log.info("Genre with id= {} not found.", id);
            return null;
        }
    }

    public List<Mpa> findAllMpa() {
        String sqlQuery = "select * from MPA_RATINGS";
        List<Mpa> mpa = jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
        if (mpa.size() == 0) {
            return null;
        }
        return mpa;
    }

    public Mpa getMpa(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select *" +
                " from MPA_RATINGS " +
                " where MPA_ID = ?", id);
        if(filmRows.next()) {
            int mpa_id = filmRows.getInt("MPA_ID");
            String name = filmRows.getString("NAME");
            return new Mpa(mpa_id, name);
        } else {
            log.info("Mpa with id= {} not found.", id);
            return null;
        }
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("GENRE_ID");
        String name = resultSet.getString("GENRE_NAME");
        return new Genre(id, name);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("MPA_ID");
        String name = resultSet.getString("NAME");
        return new Mpa(id, name);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Set<Genre> genres = new HashSet<>();
        int id = resultSet.getInt("FILM_ID");
        String name = resultSet.getString("FILM_NAME");
        String description = resultSet.getString("FILM_DESCRIPTION");
        LocalDate releaseDate = resultSet.getDate("FILM_RELEASE_DATE").toLocalDate();
        int filmDuration = resultSet.getInt("FILM_DURATION");
        Mpa mpa = new Mpa(resultSet.getInt("MPA_ID"), resultSet.getString("NAME"));
        Genre genre = new Genre(resultSet.getInt("GENRE_ID"), resultSet.getString("GENRE_NAME"));
        genres.add(genre);
        return new Film(id, name, description, releaseDate, filmDuration, mpa, genres);
    }
}
