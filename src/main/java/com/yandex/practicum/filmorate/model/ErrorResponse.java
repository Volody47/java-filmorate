package com.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}
