package com.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
public class Film {
    int id;
    String name;
    String description;
    LocalDateTime releaseDate;
    LocalTime duration;
}
