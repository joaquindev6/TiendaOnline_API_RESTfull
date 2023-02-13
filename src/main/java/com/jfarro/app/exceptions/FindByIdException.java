package com.jfarro.app.exceptions;

public class FindByIdException extends RuntimeException {

    public FindByIdException(String message) {
        super(message);
    }
}
