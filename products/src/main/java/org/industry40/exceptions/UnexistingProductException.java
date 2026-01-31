package org.industry40.exceptions;

public class UnexistingProductException extends RuntimeException {
    public UnexistingProductException(String message) {
        super(message);
    }
}
