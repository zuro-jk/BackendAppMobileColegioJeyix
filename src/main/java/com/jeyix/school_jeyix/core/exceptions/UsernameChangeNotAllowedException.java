package com.jeyix.school_jeyix.core.exceptions;

public class UsernameChangeNotAllowedException extends RuntimeException {
    public UsernameChangeNotAllowedException(String message) {
        super(message);
    }
}
