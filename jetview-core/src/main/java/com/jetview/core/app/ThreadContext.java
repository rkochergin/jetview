package com.jetview.core.app;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Roman Kochergin
 */
class ThreadContext {

    private final JetViewWebApplication application;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Set<String> staleComponentIds = ConcurrentHashMap.newKeySet();

    ThreadContext(JetViewWebApplication application, HttpServletRequest request, HttpServletResponse response) {
        this.application = application;
        this.request = request;
        this.response = response;
    }

    public JetViewWebApplication getApplication() {
        return application;
    }

    HttpServletRequest getRequest() {
        return request;
    }

    HttpServletResponse getResponse() {
        return response;
    }

    public Set<String> getStaleComponentIds() {
        return staleComponentIds;
    }
}
