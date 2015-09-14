package io.nextweb.promise.js.internal;

import delight.functional.Closure2;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

@Export
public class JsCallback implements Exportable {

    Closure2<Object, Object> closure;

    @Export
    public void perform(final Object param1, final Object param2) {
        closure.apply(param1, param2);
    }

    public JsCallback() {
        super();
    }

}
