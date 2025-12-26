package com.jetview.core.component;

import com.jetview.core.app.JetViewContext;
import com.jetview.core.component.event.Event;
import com.jetview.core.component.event.IAjaxBehavior;
import com.jetview.core.exception.JetViewRuntimeException;
import com.jetview.core.processor.IComponentPostRenderProcessor;
import com.jetview.core.renderer.IRenderer;
import com.jetview.util.Removal;
import com.jetview.util.SerializableLazyValue;
import com.jetview.util.function.SerializableSupplier;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static com.jetview.core.app.JetViewContext.*;
import static com.jetview.core.app.JetViewContext.addStaleComponent;

/**
 * @author Roman Kochergin
 */
public abstract class Component implements Renderable, Child<Component>, Serializable {

    private final SerializableLazyValue<String> lazyId = SerializableLazyValue.of(() -> getIdGenerator().generate());

    private Component parent;

    protected final Map<String, Object> model = new ConcurrentHashMap<>();

    private final Set<IAjaxBehavior> ajaxBehaviors = ConcurrentHashMap.newKeySet();

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

    public void onRequest(String event, Map<String, Object> params) {
        ajaxBehaviors.stream()
                .filter(ajaxBehavior -> ajaxBehavior.getEvent().equals(event))
                .filter(ajaxBehavior -> ajaxBehavior.getEventHandler() != null)
                .findFirst()
                .ifPresent(ajaxBehavior -> ajaxBehavior.getEventHandler().onEvent(new Event(this, event, params)));
    }

    protected Removal<?> addValue(String viewId, SerializableSupplier<?> supplier) {
        checkModelKey(viewId);
        model.put(viewId, supplier);
        return () -> {
            model.remove(viewId);
            return supplier;
        };
    }

    protected Removal<IAjaxBehavior> addBehavior(IAjaxBehavior behavior) {
        model.put(behavior.getEvent(), behavior.getCallback(this));
        ajaxBehaviors.add(behavior);
        return () -> {
            model.remove(behavior.getEvent());
            ajaxBehaviors.remove(behavior);
            return behavior;
        };
    }

    public Page getPage() {
        if (this instanceof Page page) {
            return page;
        }
        Optional<Component> parent;
        do {
            parent = getParent();
        } while (parent.isPresent() && !(parent.get() instanceof Page));
        return (Page) parent.orElseThrow();
    }

    public IRenderer getRenderer() {
        return JetViewContext.getRenderer();
    }

    @Override
    public String render() {
        var model = new HashMap<>(this.model);

        model.entrySet().stream()
                .filter(e -> e.getValue() instanceof Supplier<?>)
                .forEach(e -> model.put(e.getKey(), ((Supplier<?>) e.getValue()).get()));

        model.entrySet().stream()
                .filter(e -> e.getValue() instanceof Renderable)
                .forEach(e -> model.put(e.getKey(), ((Renderable) e.getValue()).render()));

        model.put("componentId", getId());

        var result = getRenderer().render(this, Map.copyOf(model));

        for (IComponentPostRenderProcessor processor : getComponentPostRenderProcessors()) {
            result = processor.process(result, this);
        }

        return result;
    }

    protected final void notifyStateChange() {
        if (isJetViewAjaxPageRequest()) {
            addStaleComponent(this);
        }
    }

    protected void checkModelKey(String key) {
        if (model.containsKey(key)) {
            throw new JetViewRuntimeException("ViewId '%s' already exists".formatted(key));
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
        model.entrySet().removeIf(e -> !(e.getValue() instanceof Serializable));
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
