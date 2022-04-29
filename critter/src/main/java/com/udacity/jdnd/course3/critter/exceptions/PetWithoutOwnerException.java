package com.udacity.jdnd.course3.critter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Pet has not owner")
public class PetWithoutOwnerException extends RuntimeException {
    public PetWithoutOwnerException() {}
    public PetWithoutOwnerException(String message) {
            super(message);
        }
}

