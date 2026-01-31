package com.jetview.examples.elements;

import com.jetview.core.component.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class CustomComponent extends Component {

    @Override
    protected void onLoad() {
        setTextContent(UUID.randomUUID().toString());
        setTitle("Custom component title");
    }

    @Override
    public void onRequest(String event, Map<String, Serializable> params) {
        System.out.println("Custom component received event: " + event + ", params: " + params);
    }

    public void setTitle(String title) {
        setAttribute("title", title);
    }

    public void setText(String text) {
        setTextContent(text);
    }

    public void setEnabled(boolean enabled) {
        toggleAttribute("disabled");
    }

    private void setAttribute(String name, String value) {
        execJs("this.setAttribute('%s', '%s')".formatted(name, value));
    }

    private void toggleAttribute(String name) {
        execJs("this.toggleAttribute('%s')".formatted(name));
    }

    private void setTextContent(String textContent) {
        execJs("this.textContent = '%s'".formatted(textContent));
    }

    private void execJs(String js) {
        notifyStateChange(Map.of("js", js));
    }

    @Override
    public String render() {
        return "<button is='jv-button' data-jv-id='%s'></button>".formatted(getId());
    }
}
