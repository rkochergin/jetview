package com.jetview.examples.bootstrap;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Renderable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.jetview.core.app.JetViewContext.getRenderer;

@View("templates/bootstrap/Element.peb")
public class Element implements Renderable {

    private final String tagName;
    private String text;

    private final Map<String, Object> attributes = new LinkedHashMap<>();

    public Element(String tagName) {
        this.tagName = tagName;
    }

    public Element setAttribute(String name, String value) {
        attributes.put(name, value);
        return this;
    }

    public Element setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public String render() {
        var model = new HashMap<String, Object>();
        model.put("tagName", tagName);
        model.put("attributes", attributes);
        model.put("text", text);
        return getRenderer().render(this, model);
    }
}
