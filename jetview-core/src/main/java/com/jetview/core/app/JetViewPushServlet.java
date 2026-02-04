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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Roman Kochergin
 */
@WebServlet(urlPatterns = "/jetview-push", asyncSupported = true)
public class JetViewPushServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(JetViewPushServlet.class);

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
                var ctx = event.getAsyncContext();
                var clientId = CLIENTS.remove(ctx);
                if (clientId != null) {
                    logger.atDebug()
                            .setMessage(() -> "Push client '{}' completed, total size: {}")
                            .addArgument(clientId)
                            .addArgument(CLIENTS.size())
                            .log();
                }
            }

            @Override
            public void onTimeout(AsyncEvent event) {
                var ctx = event.getAsyncContext();
                var clientId = CLIENTS.remove(ctx);
                if (clientId != null) {
                    logger.atDebug()
                            .setMessage(() -> "Push client '{}' timeout, total size: {}")
                            .addArgument(clientId)
                            .addArgument(CLIENTS.size())
                            .log();
                }
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

        var clientId = req.getParameter("id");
        CLIENTS.put(context, clientId);

        logger.atDebug()
                .setMessage(() -> "Push client '{}' connected, total size: {}")
                .addArgument(clientId)
                .addArgument(CLIENTS.size())
                .log();
    }

    public static void sendToClient(String clientId, Map<String, Object> data) {
        CLIENTS.entrySet().stream()
                .filter(entry -> entry.getValue().equals(clientId))
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

    public static void removeClient(String clientId) {
        CLIENTS.entrySet().stream()
                .filter(entry -> entry.getValue().equals(clientId))
                .findAny()
                .map(Map.Entry::getKey)
                .ifPresent(context -> {
                    context.complete();
                    CLIENTS.remove(context);
                });
    }

}