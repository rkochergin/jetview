package com.jetview.core.app;

import com.jetview.core.converter.*;
import com.jetview.core.exception.JetViewRuntimeException;
import com.jetview.core.service.IPageService;
import com.jetview.core.service.PageService;
import com.jetview.util.LazyValue;
import jakarta.servlet.GenericServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static com.jetview.core.app.JetViewContext.isJetViewAjaxPageRequest;

/**
 * @author Roman Kochergin
 */
public class JetViewWebApplication {

    private static final Logger logger = LoggerFactory.getLogger(JetViewWebApplication.class);

    private static final ThreadLocal<ThreadContext> THREAD_CONTEXT = new ThreadLocal<>();

    private ApplicationConfig applicationConfig;
    private ServletContext servletContext;
    private ApplicationContext applicationContext;

    protected ApplicationContext createApplicationContext() {
        return new DefaultApplicationContext.Builder().build();
    }

    protected final void startup(ApplicationConfig applicationConfig, ServletContext servletContext) {
        this.applicationConfig = applicationConfig;
        this.servletContext = servletContext;

        servletContext.addServlet(applicationConfig.name(), new JetViewWebApplicationServlet(this))
                .addMapping(applicationConfig.url());

        this.applicationContext = createApplicationContext();
        var resourceFactory = applicationContext.getResourceFactory();
        var converterFactory = applicationContext.getConverterFactory();
        resourceFactory.registerResource(IPageService.class, LazyValue.of(PageService::new));
        converterFactory.registerConverter(String.class, new StringToStringConverter());
        converterFactory.registerConverter(Set.of(Boolean.class, boolean.class), new StringToBooleanConverter());
        converterFactory.registerConverter(Set.of(Byte.class, byte.class), new StringToByteConverter());
        converterFactory.registerConverter(Set.of(Short.class, short.class), new StringToShortConverter());
        converterFactory.registerConverter(Set.of(Integer.class, int.class), new StringToIntegerConverter());
        converterFactory.registerConverter(Set.of(Long.class, long.class), new StringToLongConverter());
        converterFactory.registerConverter(Set.of(Float.class, float.class), new StringToFloatConverter());
        converterFactory.registerConverter(Set.of(Double.class, double.class), new StringToDoubleConverter());

        init();

        logger.atDebug()
                .setMessage(() -> "Init app '{}' with context path '{}'")
                .addArgument(applicationConfig.name())
                .addArgument(applicationConfig.url())
                .log();
    }

    protected void init() {
        // do nothing
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            THREAD_CONTEXT.set(new ThreadContext(this, request, response));
            if ("GET".equals(request.getMethod())) {
                doGet(request, response);
            } else if ("POST".equals(request.getMethod())) {
                doPost(request, response);
            }
        } finally {
            THREAD_CONTEXT.remove();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var path = JetViewRequest.getPathInfo();
        var pageService = applicationContext.getResourceFactory().getResource(IPageService.class);
        var pageClass = pageService.findPageClass(applicationConfig.pageScanPackages(), request)
                .orElseThrow(() -> new JetViewRuntimeException("Page not found for path: " + path));
        pageService.renderPage(pageClass, request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isJetViewAjaxPageRequest()) {
            var pageService = applicationContext.getResourceFactory().getResource(IPageService.class);
            if (pageService.getPage(request).isPresent()) {
                pageService.renderAjaxPage(request, response);
            } else {
                logger.atWarn()
                        .setMessage(() -> "Page is not found")
                        .log();
            }
        }
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    static ThreadContext getThreadContext() {
        return THREAD_CONTEXT.get();
    }

    static class JetViewWebApplicationServlet extends GenericServlet {

        private final transient JetViewWebApplication application;

        JetViewWebApplicationServlet(JetViewWebApplication application) {
            this.application = application;
        }

        @Override
        public void service(ServletRequest request, ServletResponse response) throws IOException {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            if (shouldIgnorePath(httpServletRequest)) {
                logger.atDebug()
                        .setMessage(() -> "Ignoring request '{}'")
                        .addArgument(httpServletRequest.getRequestURL())
                        .log();
            } else {
                application.service(httpServletRequest, httpServletResponse);
            }
        }

        private boolean shouldIgnorePath(HttpServletRequest request) {
            var ignoredPaths = application.getApplicationConfig().ignoredPaths();
            var ignore = false;
            if (ignoredPaths.length > 0) {
                var relativePath = Optional.ofNullable(request.getPathInfo()).orElse("/");
                for (var path : ignoredPaths) {
                    if (relativePath.startsWith(path)) {
                        ignore = true;
                        break;
                    }
                }
            }
            return ignore;
        }
    }
}
