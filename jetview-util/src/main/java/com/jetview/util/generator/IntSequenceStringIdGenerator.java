package com.jetview.util.generator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Roman Kochergin
 */
public class IntSequenceStringIdGenerator implements IdGenerator<String> {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

    @Override
    public String generate() {
        return String.valueOf(ID_GENERATOR.incrementAndGet());
    }
}
