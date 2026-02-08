package com.jetview.examples.elements;

/**
 * @author Roman Kochergin
 */
public interface Element<T extends Element<T, L>, L> extends Node<T, L> {
    String getTagName();
    String getOuterHtml();
    T setAttribute(String attribute, String value);
}
