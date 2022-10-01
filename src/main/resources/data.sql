merge into MPA_RATINGS (mpa_id, name)
    values
           (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

merge into GENRES (GENRE_ID, GENRE_NAME)
    values
    (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Thriller'),
    (5, 'Documentary'),
    (6, 'Action');

-- merge into FILMS_GENDER (FILM_ID, GENRE_ID)
--     values
--     (1, 2);

