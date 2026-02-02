package com.jetview.core.app;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Roman Kochergin
 */
class ThreadContext {

    private final JetViewWebApplication application;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Map<String, List<Map<String, Object>>> staleComponents = new ConcurrentHashMap<>();

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

    public Map<String, List<Map<String, Object>>> getStaleComponents() {
        return staleComponents;
    }
}
