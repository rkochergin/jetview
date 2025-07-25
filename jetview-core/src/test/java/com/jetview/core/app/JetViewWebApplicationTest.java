package com.jetview.core.app;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Roman Kochergin
 */
class JetViewWebApplicationTest {

    private ListAppender<ILoggingEvent> logAppender;

    @BeforeEach
    void setUp() {
        var logger = (Logger) LoggerFactory.getLogger(JetViewWebApplication.class);
        logAppender = new ListAppender<>();
        logAppender.start();
        logger.addAppender(logAppender);
    }

    @Test
    void init() {
        var config = mockApplicationConfig();
        var servletContext = mockServletContext();
        var application = spy(JetViewWebApplication.class);
        application.startup(config, servletContext);

        verify(application, times(1)).init();
        assertLogMessage("Init app 'TestApp' with context path '/jetview'");
    }

    @Test
    void doGet() throws IOException {
        var config = mockApplicationConfig();
        var servletContext = mockServletContext();
        var application = spy(JetViewWebApplication.class);
        application.startup(config, servletContext);

        var session = mockSession();
        var request = mockGetRequest(session);
        var response = mockResponse();

        application.service(request, response);

        var output = response.getWriter().toString();
        assertTrue(output.contains("This is Awesome Page"));
        assertTrue(output.contains("Counter value is 10"));
    }

    @Test
    void doPost() throws IOException {
        var config = mockApplicationConfig();
        var servletContext = mockServletContext();
        var application = spy(JetViewWebApplication.class);
        application.startup(config, servletContext);

        var session = mockSession();

        // first request to store page in the session
        var request = mockGetRequest(session);
        var response = mockResponse();
        application.service(request, response);

        request = mockPostRequest(session);
        response = mockResponse();
        var requestBody = "{\"id\": \"1\", \"event\": \"onclick\"}";
        var reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);
        application.service(request, response);

        var output = response.getWriter().toString();
        assertTrue(output.contains("Counter value is 11"));
    }

    @Test
    void doPost_PageNotFound() throws IOException {
        var config = mockApplicationConfig();
        var servletContext = mockServletContext();
        var application = spy(JetViewWebApplication.class);
        application.startup(config, servletContext);

        var session = mockSession();
        var request = mockPostRequest(session);
        var response = mockResponse();
        application.service(request, response);

        assertLogMessage("Page is not found");
    }

    private static ApplicationConfig mockApplicationConfig() {
        return new ApplicationConfig("TestApp", "/jetview",
                new String[]{"com.jetview.core.app"}, new String[0]);
    }

    private static ServletContext mockServletContext() {
        var servletContext = mock(ServletContext.class);
        var registration = mock(ServletRegistration.Dynamic.class);
        when(servletContext.addServlet(anyString(), any(Servlet.class))).thenReturn(registration);
        return servletContext;
    }

    private static HttpSession mockSession() {
        var session = mock(HttpSession.class);
        var attributes = new HashMap<String, Object>();
        doAnswer(invocation -> {
            attributes.put((String) invocation.getArguments()[0], invocation.getArguments()[1]);
            return null;
        }).when(session).setAttribute(anyString(), any());
        doAnswer(invocation -> attributes.get((String) invocation.getArguments()[0])).when(session).getAttribute(anyString());
        return session;
    }

    private static HttpServletRequest mockGetRequest(HttpSession session) {
        var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getServletPath()).thenReturn("/");
        when(request.getParameter("title")).thenReturn("Awesome Page");
        when(request.getParameter("counter")).thenReturn("10");
        when(request.getSession()).thenReturn(session);
        return request;
    }

    private static HttpServletRequest mockPostRequest(HttpSession session) {
        var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("POST");
        when(request.getPathInfo()).thenReturn(JetViewContext.JET_VIEW_AJAX_PAGE_SERVLET_PATH);
        when(request.getSession()).thenReturn(session);
        return request;
    }

    private static HttpServletResponse mockResponse() throws IOException {
        var response = mock(HttpServletResponse.class);
        var stringWriter = new StringWriter();
        var writer = new PrintWriter(stringWriter) {
            @Override
            public String toString() {
                flush();
                return out.toString();
            }
        };
        when(response.getWriter()).thenReturn(writer);
        return response;
    }

    private void assertLogMessage(String expectedMessage) {
        assertEquals(expectedMessage, logAppender.list.getLast().getFormattedMessage());
    }
}