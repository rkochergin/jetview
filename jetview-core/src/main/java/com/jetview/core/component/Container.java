package com.jetview.core.component;

import com.jetview.util.Removal;

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
    public Stream<? extends Component> getChildren() {
        return components.stream();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<? extends Component> traverse() {
        return Stream.concat(Stream.of(this),
                getChildren().flatMap(component ->
                        (component instanceof Traversable<?>) ?
                                ((Traversable<Component>) component).traverse() : Stream.of(component)));
    }

    protected Removal<Component> addComponent(String viewId, Component component) {
        checkModelKey(viewId);
        if (component != this && components.add(component)) {
            component.setParent(this);
            model.put(viewId, component);
            notifyStateChange();
            return () -> {
                if (components.remove(component)) {
                    component.setParent(null);
                    model.remove(viewId);
                    notifyStateChange();
                }
                return component;
            };
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Container container = (Container) o;
        return Objects.equals(components, container.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), components);
    }
}
