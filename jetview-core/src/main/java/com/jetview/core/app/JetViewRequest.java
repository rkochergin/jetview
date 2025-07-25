package com.jetview.core.app;

import com.jetview.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Roman Kochergin
 */
public class JetViewRequest {

    public static HttpServletRequest getRequest() {
        return JetViewContext.getRequest();
    }

    public static String getPathInfo() {
        var pathInfo = getRequest().getPathInfo();
        return StringUtils.hasText(pathInfo) ? pathInfo : "/";
    }
}
