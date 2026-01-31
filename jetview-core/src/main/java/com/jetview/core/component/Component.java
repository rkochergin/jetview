package com.jetview.core.component;

import com.jetview.core.component.event.Event;
import com.jetview.core.component.event.IEventHandler;
import com.jetview.core.component.model.ComponentModel;
import com.jetview.core.component.model.Model;
import com.jetview.core.processor.IComponentPostRenderProcessor;
import com.jetview.util.SerializableLazyValue;
import com.jetview.util.function.SerializableSupplier;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.jetview.core.app.JetViewContext.*;

/**
 * @author Roman Kochergin
 */
public class Component implements Child<Component>, Renderable, Serializable {

    private final SerializableLazyValue<String> lazyId = SerializableLazyValue.of(() -> getIdGenerator().generate());

    private Component parent;

    private final Model model = new ComponentModel();

    private final Map<String, IEventHandler> listeners = new ConcurrentHashMap<>();

    public String getId() {
        return lazyId.get();
    }

    @Override
    public Optional<Component> getParent() {
        return Optional.ofNullable(parent);
    }

    protected void setParent(Component parent) {
        this.parent = parent;
    }

    public void onRequest(String event, Map<String, Serializable> params) {
        var listener = listeners.get(event);
        if (listener != null) {
            listener.onEvent(new Event(this, event, params));
        }
    }

    protected void onLoad() {}

    @Override
    public String render() {
        var properties = new HashMap<String, Object>();

        model.getPropertyNames().forEach(name -> properties.put(name, model.getProperty(name).get()));
        properties.entrySet().stream()
                .filter(e -> e.getValue() instanceof Renderable)
                .forEach(e -> properties.put(e.getKey(), ((Renderable) e.getValue()).render()));

        var result = getRenderer().render(this, Map.copyOf(properties));

        for (IComponentPostRenderProcessor processor : getComponentPostRenderProcessors()) {
            result = processor.process(result, this);
        }

        return result;
    }

    public Page getPage() {
        if (this instanceof Page page) {
            return page;
        }
        Optional<Component> ancestor;
        do {
            ancestor = getParent();
        } while (ancestor.isPresent() && !(ancestor.get() instanceof Page));
        return (Page) ancestor.orElseThrow();
    }

    protected void setProperty(String name, SerializableSupplier<Object> value) {
        model.setProperty(name, value);
    }

    protected <T> T getPropertyValue(String name, Class<T> type) {
        return model.getPropertyValue(name, type);
    }

    protected boolean hasProperty(String name) {
        return model.hasProperty(name);
    }

    protected void removeProperty(String name) {
        model.removeProperty(name);
    }

    protected void setListener(String name, IEventHandler eventHandler) {
        Objects.requireNonNull(name, "name is null");
        if (eventHandler == null) {
            listeners.remove(name);
        }
        listeners.put(name, eventHandler);
    }

    protected boolean hasListener(String name) {
        return listeners.containsKey(name);
    }

    protected void removeListener(String name) {
        setListener(name, null);
    }

    protected final void notifyStateChange() {
        notifyStateChange(null);
    }

    protected final void notifyStateChange(Map<String, Serializable> data) {
        if (isJetViewAjaxPageRequest()) {
            addStaleComponent(this, data);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Component that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
