package com.example.assignment.appdomain.sanctionedperson.exception;

public class NoSuchPersonException extends RuntimeException {

    public NoSuchPersonException() {
        super("Person not found");
    }
}
