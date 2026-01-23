package com.jetview.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jetview.core.annotation.Path;
import com.jetview.core.annotation.RequestParam;
import com.jetview.core.annotation.ValueConstants;
import com.jetview.core.app.JetViewRequest;
import com.jetview.core.component.Page;
import com.jetview.core.exception.JetViewRuntimeException;
import com.jetview.util.ClassUtils;
import com.jetview.util.MimeTypes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.jetview.core.app.JetViewContext.*;

public class PageService implements IPageService {

    public static final String PAGE_ATTRIBUTE_TMPL = "_JET_VIEW_%s_CURRENT_PAGE_";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, Class<? extends Page>> pageClassCache = new ConcurrentHashMap<>();

    @Override
    public Optional<Class<? extends Page>> findPageClass(String[] pageScanPackages, HttpServletRequest request) {
        var path = JetViewRequest.getPathInfo();
        Class<? extends Page> cls = pageClassCache.computeIfAbsent(path, unused ->
                findPageClass(pageScanPackages, path).orElse(null));
        return Optional.ofNullable(cls);
    }

    @Override
    public void renderPage(Class<? extends Page> pageClass, HttpServletRequest request, HttpServletResponse response) throws IOException {
        var pageInterceptors = getPageInterceptors();
        var session = request.getSession();
        for (var interceptor : pageInterceptors) {
            boolean keepProcessing = interceptor.preCreate(pageClass);
            if (!keepProcessing) {
                return;
            }
        }
        var page = createPageInstance(pageClass);
        for (var interceptor : pageInterceptors) {
            boolean keepProcessing = interceptor.preRender(page);
            if (!keepProcessing) {
                return;
            }
        }
        renderPage(response, page);
        session.setAttribute(getPageAttributeName(), page);
        response.setStatus(HttpServletResponse.SC_OK);
        for (var interceptor : pageInterceptors) {
            interceptor.postRender(page);
        }
    }

    @Override
    public Optional<Page> getPage(HttpServletRequest request) {
        var session = request.getSession();
        var page = (Page) session.getAttribute(getPageAttributeName());
        return Optional.ofNullable(page);
    }

    @Override
    public void renderAjaxPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var page = getPage(request).orElseThrow();

        var requestPayload = objectMapper.readValue(request.getReader(), AjaxRequestPayload.class);

        page.traverse()
                .filter(component -> component.getId().equals(requestPayload.id()))
                .findFirst()
                .ifPresent(component -> component.onRequest(requestPayload.event(), requestPayload.data()));

        var staleComponents = getStaleComponents();

        var responsePayload = page.traverse()
                .filter(c -> staleComponents.containsKey(c.getId()))
                .map(c -> {
                    var data = staleComponents.get(c.getId());
                    return new AjaxResponsePayload(
                            c.getId(),
                            data.isEmpty() ? List.of(Map.of("default_markup", c.render())) : data);
                })
                .toList();

        if (!responsePayload.isEmpty()) {
            response.setContentType(MimeTypes.APPLICATION_JSON);
            var out = response.getWriter();
            out.print(objectMapper.writeValueAsString(responsePayload));
        }
    }

    private void renderPage(HttpServletResponse response, Page page) throws IOException {
        response.setContentType(page.getContentType());
        var out = response.getWriter();
        out.println(page.render());
    }

    @SuppressWarnings("unchecked")
    private static Optional<Class<? extends Page>> findPageClass(String[] pageScanPackages, String path) {
        var reflections = new Reflections(new ConfigurationBuilder().forPackages(pageScanPackages));
        Set<Class<? extends Page>> annotatedClasses = reflections.getTypesAnnotatedWith(Path.class).stream()
                .filter(clazz -> {
                    Path annotation = ClassUtils.getAnnotation(clazz, Path.class).orElseThrow();
                    String value = annotation.value();
                    return value.equals(path);
                })
                .filter(clazz -> Arrays.stream(pageScanPackages)
                        .anyMatch(p -> clazz.getCanonicalName().startsWith(p)))
                .map(clazz -> (Class<? extends Page>) clazz)
                .collect(Collectors.toSet());
        if (annotatedClasses.size() > 1) {
            throw new JetViewRuntimeException(("Multiple '@Path' annotated classes with " +
                    "the same value '%s' found in classpath").formatted(path));
        }
        return annotatedClasses.stream().findFirst();
    }

    private static Page createPageInstance(Class<?> pageClass) {
        Constructor<?>[] declaredConstructors = pageClass.getDeclaredConstructors();
        Constructor<?> declaredConstructor = declaredConstructors[0];
        declaredConstructor.setAccessible(true);
        Parameter[] parameters = declaredConstructor.getParameters();
        Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        Annotation[][] parameterAnnotations = declaredConstructor.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] paramAnnotation = parameterAnnotations[i];
            if (paramAnnotation.length == 0) {
                throw new JetViewRuntimeException("Parameter '%s' with type '%s' at index %s has to be annotated with '%s'"
                        .formatted(parameters[i].getName(), parameterTypes[i], i, "@RequestParam"));
            }
            for (Annotation annotation : paramAnnotation) {
                if (annotation instanceof RequestParam requestParam) {
                    String paramName = requestParam.value().isBlank() ? requestParam.name() : requestParam.value();
                    String paramValue = getRequest().getParameter(paramName);
                    paramValue = Objects.isNull(paramValue) ? requestParam.defaultValue() : paramValue;
                    args[i] = !Objects.equals(ValueConstants.DEFAULT_NONE, paramValue) ? getConverterFactory().getConverter(parameterTypes[i]).convert(paramValue) : null;
                }
            }
        }
        try {
            return (Page) declaredConstructor.newInstance(args);
        } catch (ReflectiveOperationException e) {
            throw new JetViewRuntimeException(e);
        }
    }

    private static String getPageAttributeName() {
        var appName = getApplication().getApplicationConfig().name();
        return PAGE_ATTRIBUTE_TMPL.formatted(appName);
    }

    private record AjaxRequestPayload(String id, String event, Map<String, Serializable> data) {
    }

    private record AjaxResponsePayload(String id, List<Map<String, Serializable>> data) {
    }
}
