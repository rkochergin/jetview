package com.jetview.core.app;

import com.jetview.core.factory.ConverterFactory;
import com.jetview.core.factory.IConverterFactory;
import com.jetview.core.factory.IResourceFactory;
import com.jetview.core.factory.ResourceFactory;
import com.jetview.core.interceptor.IPageInterceptor;
import com.jetview.core.processor.*;
import com.jetview.core.renderer.IRenderer;
import com.jetview.core.renderer.PebbleTemplateRenderer;
import com.jetview.util.generator.LongSequenceUniqueStringValueGenerator;
import com.jetview.util.generator.UniqueValueGenerator;
import io.pebbletemplates.pebble.PebbleEngine;

import java.util.Set;

/**
 * @author Roman Kochergin
 */
public class DefaultApplicationContext implements ApplicationContext {

    private final IResourceFactory resourceFactory;
    private final IConverterFactory<String, Object> converterFactory;
    private final IRenderer renderer;
    private final UniqueValueGenerator<String> uniqueValueGenerator;
    private final Set<IPageInterceptor> pageInterceptors;
    private final Set<IComponentPostRenderProcessor> componentPostRenderProcessors;

    private DefaultApplicationContext(Builder builder) {
        this.resourceFactory = builder.resourceFactory;
        this.converterFactory = builder.converterFactory;
        this.renderer = builder.renderer;
        this.uniqueValueGenerator = builder.uniqueValueGenerator;
        this.pageInterceptors = builder.pageInterceptors;
        this.componentPostRenderProcessors = builder.componentPostRenderProcessors;
    }

    public static class Builder {

        private IResourceFactory resourceFactory;
        private IConverterFactory<String, Object> converterFactory;
        private IRenderer renderer;
        private UniqueValueGenerator<String> uniqueValueGenerator;
        private Set<IPageInterceptor> pageInterceptors;
        private Set<IComponentPostRenderProcessor> componentPostRenderProcessors;

        public Builder() {
            this.resourceFactory = new ResourceFactory();
            this.converterFactory = new ConverterFactory<>();
            this.renderer = new PebbleTemplateRenderer(new PebbleEngine.Builder()
                    .autoEscaping(false)
                    .build());
            this.uniqueValueGenerator = new LongSequenceUniqueStringValueGenerator();
            this.pageInterceptors = Set.of();
            this.componentPostRenderProcessors = Set.of(
                    new RemoveJavaScriptComponentPostRenderProcessor(),
                    new SetComponentAttributesPostRenderProcessor(),
                    new PageBodyOnlyComponentPostRenderProcessor());
        }

        public Builder resourceFactory(IResourceFactory resourceFactory) {
            this.resourceFactory = resourceFactory;
            return this;
        }

        public Builder converterFactory(IConverterFactory<String, Object> converterFactory) {
            this.converterFactory = converterFactory;
            return this;
        }

        public Builder renderer(IRenderer renderer) {
            this.renderer = renderer;
            return this;
        }

        public Builder uniqueValueGenerator(UniqueValueGenerator<String> uniqueValueGenerator) {
            this.uniqueValueGenerator = uniqueValueGenerator;
            return this;
        }

        public Builder pageInterceptors(Set<IPageInterceptor> pageInterceptors) {
            this.pageInterceptors = pageInterceptors;
            return this;
        }

        public Builder componentPostRenderProcessors(Set<IComponentPostRenderProcessor> componentPostRenderProcessors) {
            this.componentPostRenderProcessors = componentPostRenderProcessors;
            return this;
        }

        public DefaultApplicationContext build() {
            return new DefaultApplicationContext(this);
        }
    }

    @Override
    public IResourceFactory getResourceFactory() {
        return resourceFactory;
    }

    @Override
    public IConverterFactory<String, Object> getConverterFactory() {
        return converterFactory;
    }

    @Override
    public IRenderer getRenderer() {
        return renderer;
    }

    @Override
    public UniqueValueGenerator<String> getUniqueValueGenerator() {
        return uniqueValueGenerator;
    }

    @Override
    public Set<IPageInterceptor> getPageInterceptors() {
        return pageInterceptors;
    }

    @Override
    public Set<IComponentPostRenderProcessor> getComponentPostRenderProcessors() {
        return componentPostRenderProcessors;
    }
}
