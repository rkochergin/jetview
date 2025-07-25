package com.jetview.core.component;

import com.jetview.util.Removal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
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

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
