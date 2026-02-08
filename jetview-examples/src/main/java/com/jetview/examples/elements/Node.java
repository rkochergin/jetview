package com.jetview.examples.elements;

/**
 * @author Roman Kochergin
 */
public interface Node<T extends Node<T, L>, L> extends EventTarget<L> {
    String getTextContent();
    T setTextContent(String textContent);
    T appendChild(Node<T, L> node);
    T removeChild(Node<T, L> node);
}
