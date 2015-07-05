package io.nextweb.promise.js.callbacks;

import delight.functional.Closure;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

import io.nextweb.promise.js.JsClosure;

/**
 * <p>
 * A closure which must be called by closure.apply(...) rather than
 * closure(...).
 * <p>
 * For the latter, see {@link JsClosure}.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
@Export
public final class JsObjectClosure implements Exportable {

    @NoExport
    Closure<Object> wrapped;

    @Export
    public void apply(final Object param) {
        wrapped.apply(param);
    }

    @NoExport
    public static JsObjectClosure wrap(final Closure<Object> closure) {
        final JsObjectClosure jsObjectClosure = new JsObjectClosure();
        jsObjectClosure.wrapped = closure;
        return jsObjectClosure;
    }

    public JsObjectClosure() {
        super();
    }

}
