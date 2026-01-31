package org.industry40.exceptions;

public class UnexistingItemException extends RuntimeException {
    public UnexistingItemException(String message) {
        super(message);
    }
}
