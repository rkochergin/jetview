package com.jetview.core.app;

import com.jetview.core.factory.IConverterFactory;
import com.jetview.core.factory.IResourceFactory;
import com.jetview.core.interceptor.IPageInterceptor;
import com.jetview.core.processor.IComponentPostRenderProcessor;
import com.jetview.core.renderer.IRenderer;
import com.jetview.util.generator.IdGenerator;

import java.util.Set;

/**
 * @author Roman Kochergin
 */
public interface ApplicationContext {

    IResourceFactory getResourceFactory();

    IConverterFactory<String, Object> getConverterFactory();

    IRenderer getRenderer();

    IdGenerator<String> getIdGenerator();

    Set<IPageInterceptor> getPageInterceptors();

    Set<IComponentPostRenderProcessor> getComponentPostRenderProcessors();
}
