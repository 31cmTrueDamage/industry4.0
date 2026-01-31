package org.industry40.exceptions;

public class UnexistingNotificationException extends RuntimeException {
    public UnexistingNotificationException(String message) {
        super(message);
    }
}
