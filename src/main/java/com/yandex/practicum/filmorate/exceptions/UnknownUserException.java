package com.yandex.practicum.filmorate.exceptions;

public class UnknownUserException extends RuntimeException {
    public UnknownUserException(String s) {
        super(s);
    }
}
