package com.jetview.core.component;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Roman Kochergin
 */
public class Composite<T extends Component> extends Component
        implements Parent<T>, Traversable<Component> {

    private final List<T> components = Collections.synchronizedList(new ArrayList<>());

    public Composite() {
    }

    public Composite(Collection<T> components) {
        add(components);
    }

    @SafeVarargs
    public final void add(T... components) {
        add(Arrays.asList(components));
    }

    public void add(Collection<T> components) {
        this.components.addAll(components);
        components.forEach(c -> c.setParent(this));
    }

    public boolean removeIf(Predicate<T> filter) {
        var list = components.stream().filter(filter).toList();
        list.forEach(c -> c.setParent(null));
        return components.removeAll(list);
    }

    @Override
    public Stream<T> getChildren() {
        return this.components.stream();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<Component> traverse() {
        return Stream.concat(Stream.of(this),
                getChildren().flatMap(component ->
                        (component instanceof Traversable<?>) ?
                                ((Traversable<Component>) component).traverse() : Stream.of(component)));
    }

    @Override
    public String render() {
        return components.stream()
                .map(Component::render)
                .collect(Collectors.joining());
    }
}
