package com.jetview.core.interceptor;


import com.jetview.core.component.Page;

/**
 * @author Roman Kochergin
 */
public interface IPageInterceptor {
    default boolean preCreate(Class<? extends Page> pageClass) {
        return true;
    }

    default boolean preRender(Page page) {
        return true;
    }

    default void postRender(Page page) {
    }
}
