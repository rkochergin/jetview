package com.jetview.core.util;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Renderable;
import com.jetview.util.ClassUtils;

import java.util.Optional;

public class RenderableUtils {

    public static Optional<String> findViewName(Renderable renderable) {
        return ClassUtils.getAnnotation(renderable.getClass(), View.class).map(View::value);
    }

    private RenderableUtils() {
    }
}
