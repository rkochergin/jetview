package com.jetview.examples.elements;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientElementApi {

    private final HtmlElement element;

    public ClientElementApi(HtmlElement element) {
        this.element = element;
    }

    public void setAttribute(String attribute, String value) {
        element.execJs("this.setAttribute('%s', '%s')".formatted(attribute, value));
    }

    public void setTextContent(String textContent) {
        element.execJs("this.textContent = '%s'".formatted(textContent));
    }

    public void appendChild(HtmlElement childElement) {
        element.execJs("this.insertAdjacentHTML('beforeend', '%s')".formatted(childElement.getOuterHtml()));
    }

    public void removeChild(HtmlElement childElement) {
        element.execJs("this.querySelector('[data-jv-id=\"%s\"]').remove()".formatted(childElement.getId()));
    }

    public void setEventHandler(String eventType, Set<String> eventPropertyRequirements) {
        element.execJs("this.setEventHandler('%s', [%s])"
                .formatted(eventType, eventPropertyRequirements.stream()
                        .map("'%s'"::formatted).collect(Collectors.joining(","))));
    }

    public void removeEventHandler(String eventType) {
        element.execJs("this.removeEventHandler('%s')".formatted(eventType));
    }
}
