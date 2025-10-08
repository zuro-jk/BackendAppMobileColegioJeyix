package com.jeyix.school_jeyix.core.exceptions;

public class CustomerAlreadyExistsException extends ConflictException {
    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
