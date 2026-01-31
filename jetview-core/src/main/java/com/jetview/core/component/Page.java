package com.jetview.core.component;

import com.jetview.core.app.ApplicationContext;
import com.jetview.core.app.JetViewContext;
import com.jetview.core.app.JetViewRequest;
import com.jetview.util.MimeTypes;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Roman Kochergin
 */
public class Page extends Container {

    public Page() {
        setListener("DOMContentLoaded", _ -> traverse().forEach(Component::onLoad));
    }

    public String getContentType() {
        return MimeTypes.TEXT_HTML;
    }

    public ApplicationContext getApplicationContext() {
        return JetViewContext.getApplicationContext();
    }

    public HttpServletRequest getRequest() {
        return JetViewRequest.getRequest();
    }

    public <T, S extends T> S getResource(Class<T> resourceType) {
        return getApplicationContext().getResourceFactory().getResource(resourceType);
    }

    @Override
    public String render() {
        setProperty("request", this::getRequest);
        return super.render();
    }
}
