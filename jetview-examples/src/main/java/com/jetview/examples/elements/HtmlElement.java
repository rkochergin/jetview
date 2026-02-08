package com.jetview.examples.elements;

import com.jetview.core.component.Composite;
import com.jetview.core.component.event.IEventHandler;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.jetview.core.app.JetViewContext.isJetViewAjaxPageRequest;

/**
 * @author Roman Kochergin
 */
public class HtmlElement extends Composite<HtmlElement>
        implements Element<HtmlElement, IEventHandler> {

    private final org.jsoup.nodes.Element delegate;
    private final Map<String, List<IEventHandler>> listeners = new ConcurrentHashMap<>();

    public HtmlElement(String tagName) {
        delegate = new org.jsoup.nodes.Element(tagName);
        delegate.attr("data-jv-id", getId());
        delegate.attr("is", "jv-el-%s".formatted(tagName));
    }

    @Override
    public String getTagName() {
        return delegate.tagName();
    }

    @Override
    public String getOuterHtml() {
        return delegate.outerHtml();
    }

    @Override
    public void setAttribute(String attribute, String value) {
        if (isJetViewAjaxPageRequest()) {
            execJs("this.setAttribute('%s', '%s')".formatted(attribute, value));
        } else {
            delegate.attr(attribute, value);
        }
    }

    @Override
    public String getTextContent() {
        return delegate.text();
    }

    @Override
    public void setTextContent(String textContent) {
        if (isJetViewAjaxPageRequest()) {
            execJs("this.textContent = '%s'".formatted(textContent));
        } else {
            delegate.text(textContent);
        }
    }

    @Override
    public HtmlElement appendChild(Node<HtmlElement, IEventHandler> node) {
        var child = (HtmlElement) node;
        if (isJetViewAjaxPageRequest()) {
            execJs("this.insertAdjacentHTML('beforeend', '%s')".formatted(child.delegate.outerHtml()));
        } else {
            delegate.appendChild(child.delegate);
        }
        add(child);
        return this;
    }

    @Override
    public HtmlElement removeChild(Node<HtmlElement, IEventHandler> node) {
        var child = (HtmlElement) node;
        if (isJetViewAjaxPageRequest()) {
            execJs("this.querySelector('[data-jv-id=\"%s\"]').remove()".formatted(child.getId()));
        } else {
            delegate.childNodes().remove(child.delegate);
        }
        removeIf(element -> element.getId().equals(child.getId()));
        return this;
    }

    @Override
    public void addEventListener(String eventType, IEventHandler handler) {
        addEventListener(eventType, handler, Set.of());
    }

    @Override
    public void addEventListener(String eventType, IEventHandler handler, Set<String> eventPropertyRequirements) {
        Objects.requireNonNull(handler, "handler is null");
        listeners.compute(eventType, (k, v) -> {
            if (v == null) {
                var list = new CopyOnWriteArrayList<IEventHandler>();
                list.add(handler);
                return list;
            } else {
                v.add(handler);
                return v;
            }
        });
        if (!hasListener(eventType)) {
            setListener(eventType, event ->
                    listeners.get(event.type())
                            .forEach(h -> h.onEvent(event)));
            if (isJetViewAjaxPageRequest()) {
                execJs("this.setEventHandler('%s', [%s])"
                        .formatted(eventType, eventPropertyRequirements.stream()
                                .map("'%s'"::formatted).collect(Collectors.joining(","))));
            } else {
                delegate.attr("data-jv-listener-%s".formatted(eventType), String.join(",", eventPropertyRequirements));
            }
        }
    }

    @Override
    public void removeEventListener(String eventType, IEventHandler handler) {
        Objects.requireNonNull(handler, "handler is null");
        var eventHandlers = listeners.get(eventType);
        if (eventHandlers == null) {
            return;
        }
        if (eventHandlers.remove(handler) && eventHandlers.isEmpty()) {
            if (isJetViewAjaxPageRequest()) {
                execJs("this.removeEventHandler('%s')".formatted(eventType));
            } else {
                delegate.removeAttr("data-jv-listener-%s".formatted(eventType));
            }
            removeListener(eventType);
        }
    }

    private void execJs(String js) {
        notifyStateChange(Map.of("js", js));
    }

    @Override
    public String render() {
        return getOuterHtml();
    }
}
