package com.jetview.util;

import org.jspecify.annotations.Nullable;

/**
 * @author Roman Kochergin
 */
public class StringUtils {
    /**
     * Check whether the given {@code String} contains actual <em>text</em>.
     * <p>More specifically, this method returns {@code true} if the
     * {@code String} is not {@code null}, its length is greater than 0,
     * and it contains at least one non-whitespace character.
     *
     * @param str the {@code String} to check (maybe {@code null})
     * @return {@code true} if the {@code String} is not {@code null}, its
     * length is greater than 0, and it does not contain whitespace only
     */
    public static boolean hasText(@Nullable String str) {
        return str != null && !str.isBlank();
    }

    /**
     * Capitalizes a String changing the first character to title case.
     * No other characters are changed.
     *
     * @param str the {@code String} to capitalize (maybe {@code null})
     * @return capitalized {@code String} or {@code null}
     */
    public static @Nullable String capitalize(@Nullable String str) {
        return hasText(str) ? str.substring(0, 1).toUpperCase() + str.substring(1) : str;
    }

    private StringUtils() {
    }
}
