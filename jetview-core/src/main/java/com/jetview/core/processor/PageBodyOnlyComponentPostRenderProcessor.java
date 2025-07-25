package com.jetview.core.processor;

import com.jetview.core.component.Component;
import com.jetview.core.component.Page;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jetview.core.app.JetViewContext.isJetViewAjaxPageRequest;

/**
 * @author Roman Kochergin
 */
public class PageBodyOnlyComponentPostRenderProcessor implements IComponentPostRenderProcessor {

    private final Pattern pattern = Pattern.compile("(?i)(?s)<\\s*(body)\\b[^<>]*>.*?<\\s*/\\s*(body)\\s*>", Pattern.UNICODE_CASE | Pattern.MULTILINE);

    @Override
    public String process(String output, Component component) {
        if (component instanceof Page && isJetViewAjaxPageRequest()) {
            Matcher m = pattern.matcher(output);
            if (m.find()) {
                return m.group();
            }
        }
        return output;
    }
}
