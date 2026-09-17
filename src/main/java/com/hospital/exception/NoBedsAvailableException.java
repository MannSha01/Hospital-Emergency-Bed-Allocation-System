package com.hospital.exception;

public class NoBedsAvailableException extends Exception {
    public NoBedsAvailableException(String message) {
        super(message);
    }
}
