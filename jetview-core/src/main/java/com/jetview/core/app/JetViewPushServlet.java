package com.jetview.core.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jetview.util.MimeTypes;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Roman Kochergin
 */
@WebServlet(urlPatterns = "/jetview-push", asyncSupported = true)
public class JetViewPushServlet extends HttpServlet {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Map<AsyncContext, String> CLIENTS = new ConcurrentHashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType(MimeTypes.TEXT_EVENT_STREAM);
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Connection", "keep-alive");

        AsyncContext context = req.startAsync();
        context.addListener(new AsyncListener() {

            @Override
            public void onComplete(AsyncEvent event) {
                CLIENTS.remove(context);
            }

            @Override
            public void onTimeout(AsyncEvent event) {
                CLIENTS.remove(context);
                context.complete();
            }

            @Override
            public void onError(AsyncEvent event) {
                // do nothing
            }

            @Override
            public void onStartAsync(AsyncEvent event) {
                // do nothing
            }
        });

        CLIENTS.put(context, req.getParameter("id"));
    }

    public static void sendToComponent(String componentId, Map<String, Serializable> data) {
        CLIENTS.entrySet().stream()
                .filter(entry -> entry.getValue().equals(componentId))
                .map(Map.Entry::getKey)
                .findFirst()
                .ifPresent(context -> {
                    try {
                        PrintWriter out = context.getResponse().getWriter();
                        out.write("data: " + OBJECT_MAPPER.writeValueAsString(data) + "\n\n");
                        out.flush();
                    } catch (Exception e) {
                        CLIENTS.remove(context);
                    }
                });
    }
}