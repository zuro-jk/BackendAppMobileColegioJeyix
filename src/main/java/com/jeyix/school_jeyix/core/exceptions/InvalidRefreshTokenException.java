package com.jeyix.school_jeyix.core.exceptions;

public class InvalidRefreshTokenException extends RuntimeException{

    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
