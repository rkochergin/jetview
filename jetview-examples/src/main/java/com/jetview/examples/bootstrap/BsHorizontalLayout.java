package com.jetview.examples.bootstrap;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Component;
import com.jetview.core.component.Composite;

import java.util.Map;

import static com.jetview.core.app.JetViewContext.getRenderer;

/**
 * @author Roman Kochergin
 */
@View("templates/bootstrap/HorizontalLayout.peb")
public class BsHorizontalLayout extends Composite<Component> {
    @Override
    public String render() {
        return getRenderer().render(this, Map.of("components", getChildren().toList()));
    }
}
