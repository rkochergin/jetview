package com.jetview.core.app;

import com.jetview.core.component.Component;
import com.jetview.core.factory.IConverterFactory;
import com.jetview.core.factory.IResourceFactory;
import com.jetview.core.interceptor.IPageInterceptor;
import com.jetview.core.processor.IComponentPostRenderProcessor;
import com.jetview.core.renderer.IRenderer;
import com.jetview.util.generator.IdGenerator;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;

import static com.jetview.core.app.JetViewWebApplication.getThreadContext;

/**
 * @author Roman Kochergin
 */
public class JetViewContext {

    public static final String JET_VIEW_AJAX_PAGE_SERVLET_PATH = "/jetview-ajax-page";

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

    public static IdGenerator<String> getIdGenerator() {
        return getApplicationContext().getIdGenerator();
    }

    public static Set<IPageInterceptor> getPageInterceptors() {
        return Set.copyOf(getApplicationContext().getPageInterceptors());
    }

    public static Set<IComponentPostRenderProcessor> getComponentPostRenderProcessors() {
        return Set.copyOf(getApplicationContext().getComponentPostRenderProcessors());
    }

    public static void addStaleComponent(Component component) {
        getThreadContext().getStaleComponentIds().add(component.getId());
    }

    public static Set<String> getStaleComponentIds() {
        return Set.copyOf(getThreadContext().getStaleComponentIds());
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
}
