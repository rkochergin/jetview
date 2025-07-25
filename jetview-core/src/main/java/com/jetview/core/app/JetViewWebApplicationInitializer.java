package com.jetview.core.app;

import com.jetview.core.annotation.JetViewApplication;
import com.jetview.core.exception.JetViewRuntimeException;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.HandlesTypes;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * @author Roman Kochergin
 */
@HandlesTypes({JetViewWebApplication.class})
public class JetViewWebApplicationInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> appTypes, ServletContext ctx) {
        for (Class<?> appType : appTypes) {
            try {
                if (!appType.isAnnotationPresent(JetViewApplication.class)) {
                    throw new JetViewRuntimeException("Missing annotation type:" + JetViewApplication.class);
                }
                JetViewApplication annotation = appType.getAnnotation(JetViewApplication.class);
                Constructor<?> declaredConstructor = appType.getDeclaredConstructor();
                var app = (JetViewWebApplication) declaredConstructor.newInstance();
                var name = annotation.name();
                var url = annotation.url();
                var pageScanPackages = annotation.pageScanPackages();
                var ignoredPaths = annotation.ignoredPaths();
                app.startup(new ApplicationConfig(name, url, pageScanPackages, ignoredPaths), ctx);
            } catch (ReflectiveOperationException e) {
                throw new JetViewRuntimeException(e);
            }
        }
    }
}
