package com.AlertaApagao.Location_service.exception;

public class MaxUserLocationsReachedException extends RuntimeException {
    public MaxUserLocationsReachedException(String message) {
        super(message);
    }
}
