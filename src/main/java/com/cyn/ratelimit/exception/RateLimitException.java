package com.cyn.ratelimit.exception;

/**
 * @author Godc
 * @description:
 * @date 2023/1/18 17:28
 */
public class RateLimitException extends Exception{
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public RateLimitException(String message) {
        super(message);
    }
}
