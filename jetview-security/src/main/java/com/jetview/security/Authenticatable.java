package com.jetview.security;

import java.security.Principal;

/**
 * @author Roman Kochergin
 */
public interface Authenticatable<T extends Principal> {

    void login(T principal) throws AuthenticationException;

    void logout();
}
