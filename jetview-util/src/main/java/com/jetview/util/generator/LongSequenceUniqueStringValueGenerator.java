package com.jetview.util.generator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Roman Kochergin
 */
public class LongSequenceUniqueStringValueGenerator implements UniqueValueGenerator<String> {

    private static final AtomicLong GENERATOR = new AtomicLong();

    @Override
    public String generate() {
        return String.valueOf(GENERATOR.incrementAndGet());
    }
}
