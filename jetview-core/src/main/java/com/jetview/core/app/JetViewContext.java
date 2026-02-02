package com.jetview.core.app;

import com.jetview.core.component.Component;
import com.jetview.core.factory.IConverterFactory;
import com.jetview.core.factory.IResourceFactory;
import com.jetview.core.interceptor.IPageInterceptor;
import com.jetview.core.processor.IComponentPostRenderProcessor;
import com.jetview.core.renderer.IRenderer;
import com.jetview.util.generator.UniqueValueGenerator;
import jakarta.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.*;

import static com.jetview.core.app.JetViewWebApplication.getThreadContext;

/**
 * @author Roman Kochergin
 */
public class JetViewContext {

    public static final String JET_VIEW_AJAX_PAGE_SERVLET_PATH = "/jetview-ajax";

    public static HttpServletRequest getRequest() {
        return getThreadContext().getRequest();
    }

    public static IResourceFactory getResourceFactory() {
        return getApplicationContext().getResourceFactory();
    }

    public static IConverterFactory<String, Object> getConverterFactory() {
        return getApplicationContext().getConverterFactory();
    }

    public static IRenderer getRenderer() {
        return getApplicationContext().getRenderer();
    }

    public static UniqueValueGenerator<String> getUniqueValueGenerator() {
        return getApplicationContext().getUniqueValueGenerator();
    }

    public static Set<IPageInterceptor> getPageInterceptors() {
        return Set.copyOf(getApplicationContext().getPageInterceptors());
    }

    public static Set<IComponentPostRenderProcessor> getComponentPostRenderProcessors() {
        return Set.copyOf(getApplicationContext().getComponentPostRenderProcessors());
    }

    public static void pushComponentData(Component component, Map<String, Serializable> data) {
        JetViewPushServlet.sendToComponent(component.getId(), data);
    }

    public static void addStaleComponent(Component component, Map<String, Serializable> data) {
        var staleComponents = getThreadContext().getStaleComponents();
        staleComponents.compute(component.getId(), (k, v) -> {
            if (v == null) {
                var list = new ArrayList<Map<String, Serializable>>();
                if (Objects.nonNull(data)) {
                    list.add(data);
                }
                return list;
            } else {
                if (Objects.nonNull(data)) {
                    v.add(data);
                }
                return v;
            }
        });
    }

    public static Map<String, List<Map<String, Serializable>>> getStaleComponents() {
        return Map.copyOf(getThreadContext().getStaleComponents());
    }

    public static boolean isJetViewAjaxPageRequest() {
        String path = JetViewRequest.getPathInfo();
        return path.equals(JET_VIEW_AJAX_PAGE_SERVLET_PATH);
    }

    public static ApplicationContext getApplicationContext() {
        return getApplication().getApplicationContext();
    }

    public static JetViewWebApplication getApplication() {
        return getThreadContext().getApplication();
    }

    private JetViewContext() {
    }
}
