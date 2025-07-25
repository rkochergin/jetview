package com.jetview.security;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Roman Kochergin
 */
public class DefaultAuthenticationContext implements AuthenticationContext {

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public Optional<? extends Principal> getPrincipal() {
        return Optional.empty();
    }

    @Override
    public Collection<String> getGrantedRoles() {
        return List.of();
    }

    @Override
    public boolean hasRole(String role) {
        return false;
    }

    @Override
    public boolean hasAnyRole(Collection<String> roles) {
        return false;
    }

    @Override
    public boolean hasAllRoles(Collection<String> roles) {
        return false;
    }
}
