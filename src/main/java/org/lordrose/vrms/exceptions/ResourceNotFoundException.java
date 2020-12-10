package org.lordrose.vrms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final String MESSAGE_WITH_ID = "Resource with ID: ";
    private static final String MESSAGE_WITH_VALUE = "Resource: ";
    private static final String NOT_FOUND_MESSAGE = " is not found!";

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException newExceptionWithId(String id) {
        return new ResourceNotFoundException(MESSAGE_WITH_ID + id + NOT_FOUND_MESSAGE);
    }

    public static ResourceNotFoundException newExceptionWithId(Long id) {
        return new ResourceNotFoundException(MESSAGE_WITH_ID + id + NOT_FOUND_MESSAGE);
    }

    public static ResourceNotFoundException newExceptionWithValue(String value) {
        return new ResourceNotFoundException(MESSAGE_WITH_VALUE + value + NOT_FOUND_MESSAGE);
    }
}
