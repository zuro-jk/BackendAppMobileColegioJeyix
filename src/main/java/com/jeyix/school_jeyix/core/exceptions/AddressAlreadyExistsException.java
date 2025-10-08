package com.jeyix.school_jeyix.core.exceptions;

public class AddressAlreadyExistsException extends ConflictException {
    public AddressAlreadyExistsException(String message) {
        super(message);
    }
}
