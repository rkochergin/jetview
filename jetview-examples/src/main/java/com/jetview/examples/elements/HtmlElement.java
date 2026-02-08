package com.jetview.examples.elements;

import com.jetview.core.component.Composite;
import com.jetview.core.component.event.IEventHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.jetview.core.app.JetViewContext.isJetViewAjaxPageRequest;

/**
 * @author Roman Kochergin
 */
public class HtmlElement extends Composite<HtmlElement>
        implements Element<HtmlElement, IEventHandler> {

    private final ClientElementApi clientApi;
    private final org.jsoup.nodes.Element delegate;
    private final Map<String, List<IEventHandler>> listeners = new ConcurrentHashMap<>();

    public HtmlElement(String tagName) {
        delegate = new org.jsoup.nodes.Element(tagName);
        delegate.attr("data-jv-id", getId());
        delegate.attr("is", "jv-el-%s".formatted(tagName));
        clientApi = new ClientElementApi(this);
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
    public HtmlElement setAttribute(String attribute, String value) {
        if (isJetViewAjaxPageRequest()) {
            clientApi.setAttribute(attribute, value);
        } else {
            delegate.attr(attribute, value);
        }
        return this;
    }

    @Override
    public String getTextContent() {
        return delegate.text();
    }

    @Override
    public HtmlElement setTextContent(String textContent) {
        if (isJetViewAjaxPageRequest()) {
            clientApi.setTextContent(textContent);
        } else {
            delegate.text(textContent);
        }
        return this;
    }

    @Override
    public HtmlElement appendChild(Node<HtmlElement, IEventHandler> node) {
        add((HtmlElement) node);
        return this;
    }

    @Override
    public void add(Collection<HtmlElement> children) {
        children.forEach(child -> {
            if (isJetViewAjaxPageRequest()) {
                clientApi.appendChild(child);
            } else {
                delegate.appendChild(child.delegate);
            }
        });
        super.add(children);
    }

    @Override
    public HtmlElement removeChild(Node<HtmlElement, IEventHandler> node) {
        var child = (HtmlElement) node;
        removeIf(element -> element.getId().equals(child.getId()));
        return this;
    }

    @Override
    public boolean removeIf(Predicate<HtmlElement> filter) {
        var list = getChildren().filter(filter).toList();
        var removed = super.removeIf(filter);
        if (removed) {
            list.forEach(child -> {
                if (isJetViewAjaxPageRequest()) {
                    clientApi.removeChild(child);
                } else {
                    child.delegate.remove();
                }
            });
        }
        return removed;
    }

    @Override
    public HtmlElement addEventListener(String eventType, IEventHandler handler) {
        addEventListener(eventType, handler, Set.of());
        return this;
    }

    @Override
    public HtmlElement addEventListener(String eventType, IEventHandler handler, Set<String> eventPropertyRequirements) {
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
            setListener(eventType, event -> listeners.get(event.type()).forEach(h -> {
                h.onEvent(event);
            }));
            if (isJetViewAjaxPageRequest()) {
                clientApi.setEventHandler(eventType, eventPropertyRequirements);
            } else {
                delegate.attr("data-jv-listener-%s".formatted(eventType), String.join(",", eventPropertyRequirements));
            }
        }
        return this;
    }

    @Override
    public HtmlElement removeEventListener(String eventType, IEventHandler handler) {
        Objects.requireNonNull(handler, "handler is null");
        var eventHandlers = listeners.get(eventType);
        if (eventHandlers == null) {
            return this;
        }
        if (eventHandlers.remove(handler) && eventHandlers.isEmpty()) {
            if (isJetViewAjaxPageRequest()) {
                clientApi.removeEventHandler(eventType);
            } else {
                delegate.removeAttr("data-jv-listener-%s".formatted(eventType));
            }
            removeListener(eventType);
        }
        return this;
    }

    void execJs(String js) {
        notifyStateChange(Map.of("js", js));
    }

    @Override
    public String render() {
        return getOuterHtml();
    }

    public void consume(Consumer<HtmlElement> consumer) {
        consumer.accept(this);
    }
}
