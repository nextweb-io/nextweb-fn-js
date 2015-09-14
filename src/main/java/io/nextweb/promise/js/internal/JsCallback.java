package io.nextweb.promise.js.internal;

import delight.functional.Closure2;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

@Export
public class JsCallback implements Exportable {

    Closure2<Object, Object> closure;

    @NoExport
    public static JsCallback wrap(final Closure2<Object, Object> closure) {
        final JsCallback jsCallback = new JsCallback();
        jsCallback.closure = closure;
        return jsCallback;
    }

    @Export
    public void perform(final Object param1, final Object param2) {
        closure.apply(param1, param2);
    }

    public JsCallback() {
        super();
    }

}
