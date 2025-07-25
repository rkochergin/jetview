package com.jetview.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Roman Kochergin
 */
public class AuthenticationException extends Exception {

    private final Map<String, Collection<String>> detailMessages = new HashMap<>();

    public AuthenticationException(String message) {
        super(message);
    }

    public void putDetailMessages(String key, Collection<String> messages) {
        detailMessages.put(key, messages);
    }

    public Map<String, Collection<String>> getDetailMessages() {
        return Map.copyOf(detailMessages);
    }
}
