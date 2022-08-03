package com.yandex.practicum.filmorate.model;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    @JsonIgnore
    private Set<Integer> userWhoLikedIds = new HashSet<>();
}
