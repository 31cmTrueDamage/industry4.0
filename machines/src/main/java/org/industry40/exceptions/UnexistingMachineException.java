package org.industry40.exceptions;

public class UnexistingMachineException extends RuntimeException {
    public UnexistingMachineException(String message) {
        super(message);
    }
}
