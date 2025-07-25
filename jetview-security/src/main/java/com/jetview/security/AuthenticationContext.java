package com.jetview.security;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * @author Roman Kochergin
 */
public interface AuthenticationContext {

    boolean isAuthenticated();

    Optional<? extends Principal> getPrincipal();

    Collection<String> getGrantedRoles();

    boolean hasRole(String role);

    boolean hasAnyRole(Collection<String> roles);

    default boolean hasAnyRole(String... roles) {
        return hasAnyRole(Set.of(roles));
    }

    boolean hasAllRoles(Collection<String> roles);

    default boolean hasAllRoles(String... roles) {
        return hasAllRoles(Set.of(roles));
    }
}
