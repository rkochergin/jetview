package com.jetview.core.processor;

import com.jetview.core.component.Component;

import static com.jetview.core.app.JetViewContext.isJetViewAjaxPageRequest;

/**
 * @author Roman Kochergin
 */
public class RemoveJavaScriptComponentPostRenderProcessor implements IComponentPostRenderProcessor {
    @Override
    public String process(String output, Component component) {
        if (isJetViewAjaxPageRequest()) {
            return output.replaceAll("(?i)(?s)<\\s*(script)\\b[^<>]*>.*?<\\s*/\\s*(script)\\s*>", "");
        }
        return output;
    }
}
