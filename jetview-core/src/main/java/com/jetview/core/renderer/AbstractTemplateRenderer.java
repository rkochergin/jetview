package com.jetview.core.renderer;

import com.jetview.core.component.Renderable;
import com.jetview.core.exception.JetViewRuntimeException;
import com.jetview.core.util.RenderableUtils;

import java.util.Map;

/**
 * @author Roman Kochergin
 */
public abstract class AbstractTemplateRenderer implements IRenderer {

    @Override
    public String render(Renderable renderable, Map<String, Object> model) {
        var template = RenderableUtils.findViewName(renderable)
                .orElseThrow(() -> new JetViewRuntimeException("View is not found for renderable: " + renderable));
        return render(template, model);
    }

    public abstract String render(String template, Map<String, Object> model);
}
