package com.jetview.core.renderer;

import com.jetview.core.exception.JetViewRuntimeException;
import io.pebbletemplates.pebble.PebbleEngine;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author Roman Kochergin
 */
public class PebbleTemplateRenderer extends AbstractTemplateRenderer {

    private final PebbleEngine engine;

    public PebbleTemplateRenderer(PebbleEngine engine) {
        this.engine = engine;
    }

    @Override
    public String render(String template, Map<String, Object> model) {
        var writer = new StringWriter();
        var compiledTemplate = engine.getTemplate(template);
        try {
            compiledTemplate.evaluate(writer, model);
        } catch (IOException e) {
            throw new JetViewRuntimeException(e);
        }
        return writer.toString();
    }
}
