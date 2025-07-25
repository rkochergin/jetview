package com.jetview.core.app;

/**
 * @author Roman Kochergin
 */
public record ApplicationConfig(String name, String url,
                                String[] pageScanPackages, String[] ignoredPaths) {

}
