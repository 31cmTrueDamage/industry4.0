package org.industry40.exceptions;

public class UnexistingCustomerException extends RuntimeException {
    public UnexistingCustomerException(String message) {
        super(message);
    }
}
