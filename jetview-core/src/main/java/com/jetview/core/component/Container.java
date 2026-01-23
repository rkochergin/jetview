package com.jetview.core.component;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * @author Roman Kochergin
 */
public abstract class Container extends Component
        implements Parent<Component>, Traversable<Component> {

    private final Set<Component> components = ConcurrentHashMap.newKeySet();

    @Override
    public Stream<Component> getChildren() {
        return components.stream();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<Component> traverse() {
        return Stream.concat(Stream.of(this),
                getChildren().flatMap(component ->
                        (component instanceof Traversable<?>) ?
                                ((Traversable<Component>) component).traverse() : Stream.of(component)));
    }

    protected void setComponent(String name, Component component) {
        Objects.requireNonNull(name, "name is null");
        if (component == null) {
            var c = getPropertyValue(name, Component.class);
            if (c != null && components.remove(c)) {
                c.setParent(null);
                removeProperty(name);
                notifyStateChange();
            }
        } else if (component != this && components.add(component)) {
            component.setParent(this);
            setProperty(name, () -> component);
            notifyStateChange();
        }
    }
}
