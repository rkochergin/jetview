package com.jetview.util;

import java.io.Serializable;

public interface Removal<T> extends Serializable {
    T remove();
}
