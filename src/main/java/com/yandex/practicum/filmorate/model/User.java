package com.yandex.practicum.filmorate.model;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @JsonIgnore
    private Set<Integer> friendIds = new HashSet<>();

    public User(int user_id, String user_email, String user_login, String user_full_name, LocalDate user_birthday) {
        this.id = user_id;
        this.email = user_email;
        this.login = user_login;
        this.name = user_full_name;
        this.birthday = user_birthday;

    }

}
