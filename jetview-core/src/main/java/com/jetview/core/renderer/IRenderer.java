package com.jetview.core.renderer;


import com.jetview.core.component.Renderable;

import java.util.Map;

public interface IRenderer {
    String render(Renderable renderable, Map<String, Object> model);
}
