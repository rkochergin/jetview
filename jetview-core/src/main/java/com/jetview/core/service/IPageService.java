package com.jetview.core.service;

import com.jetview.core.component.Page;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public interface IPageService {
    Optional<Class<? extends Page>> findPageClass(String[] pageScanPackages, HttpServletRequest request);

    void renderPage(Class<? extends Page> pageClass, HttpServletRequest request, HttpServletResponse response) throws IOException;

    Optional<Page> getPage(HttpServletRequest request);

    void processAjaxRequest(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
