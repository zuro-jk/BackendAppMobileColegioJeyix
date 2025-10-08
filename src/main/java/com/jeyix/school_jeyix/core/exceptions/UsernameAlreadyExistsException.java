package com.jeyix.school_jeyix.core.exceptions;

public class UsernameAlreadyExistsException extends ConflictException{
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
