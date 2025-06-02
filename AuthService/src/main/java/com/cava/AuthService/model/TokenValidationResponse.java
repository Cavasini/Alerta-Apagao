package com.cava.AuthService.model;

public record TokenValidationResponse(boolean valid, String message, String payload) {
}
