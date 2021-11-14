package io.okami101.realworld.api.exception;

public class InvalidAuthenticationException extends RuntimeException {
    public InvalidAuthenticationException() {
        super("invalid email or password");
    }
}
