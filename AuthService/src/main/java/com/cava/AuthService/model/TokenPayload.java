package com.cava.AuthService.model;

import java.util.List;

public class TokenPayload {
    private String userId;
    private String email;
    private List<String> roles;

    public TokenPayload(String userId, String email, List<String> roles) {
        this.userId = userId;
        this.email = email;
        this.roles = roles;
    }
}
