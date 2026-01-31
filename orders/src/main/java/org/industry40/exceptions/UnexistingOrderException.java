package org.industry40.exceptions;

public class UnexistingOrderException extends RuntimeException {
    public UnexistingOrderException(String message) {
        super(message);
    }
}
