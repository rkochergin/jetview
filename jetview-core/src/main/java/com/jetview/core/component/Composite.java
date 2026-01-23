package com.jetview.core.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public Composite(List<T> components) {
        this.components.addAll(components);
    }

    public void add(T component) {
        components.add(component);
    }

    public boolean removeIf(Predicate<T> filter) {
        return components.removeIf(filter);
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
