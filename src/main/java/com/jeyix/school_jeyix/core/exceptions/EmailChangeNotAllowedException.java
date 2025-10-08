package com.jeyix.school_jeyix.core.exceptions;

public class EmailChangeNotAllowedException extends RuntimeException {
    public EmailChangeNotAllowedException(String message) {
        super(message);
    }
}
