CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_full_name VARCHAR(255) NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    user_birthday DATE,
    user_login VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
                                     film_id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                     film_name VARCHAR(255) NOT NULL,
                                     film_description VARCHAR(255) NOT NULL,
                                     film_release_date DATE,
                                     film_duration INTEGER NOT NULL,
                                     mpa_id int not null,
                                     foreign key (mpa_id) references mpa_ratings(mpa_id)
);

CREATE TABLE IF NOT EXISTS films_like (
                                         user_id int not null,
                                         film_id int not null,
                                         foreign key (user_id) references users(user_id),
                                         foreign key (film_id) references films(film_id)
);

CREATE TABLE IF NOT EXISTS friends (
                                          user_id int not null,
                                          friend_id int not null,
                                          request_status int,
                                          friend_relationship_status boolean,
                                          foreign key (user_id) references users(user_id)
);

CREATE TABLE IF NOT EXISTS genres (
                                      genre_id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                      genre_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS films_gender (
                                            film_id int not null,
                                            genre_id int not null,
                                            foreign key (film_id) references films(film_id),
                                            foreign key (genre_id) references genres(genre_id)
);

CREATE TABLE IF NOT EXISTS mpa_ratings (
                                           mpa_id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                           name VARCHAR(255) NOT NULL
);

