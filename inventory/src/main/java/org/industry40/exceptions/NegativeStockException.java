package org.industry40.exceptions;

public class NegativeStockException extends RuntimeException {
    public NegativeStockException(String message) {
        super(message);
    }
}
