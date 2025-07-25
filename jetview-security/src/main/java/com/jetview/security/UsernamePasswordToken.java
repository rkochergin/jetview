package com.jetview.security;

import java.security.Principal;

/**
 * @author Roman Kochergin
 */
public record UsernamePasswordToken(String username, String password) implements Principal {
    @Override
    public String getName() {
        return username();
    }
}
