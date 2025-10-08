package com.jeyix.school_jeyix.core.exceptions;

public class DuplicateReservationException extends ConflictException {
    public DuplicateReservationException(String message) {
        super(message);
    }
}
