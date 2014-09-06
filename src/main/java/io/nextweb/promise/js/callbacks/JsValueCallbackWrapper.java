package io.nextweb.promise.js.callbacks;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.callbacks.ValueCallback;

@Export
public class JsValueCallbackWrapper {

    @NoExport
    ValueCallback<Object> wrapped;

    @Export
    public void onSuccess(final JavaScriptObject value) {

    }

    @Export
    public void onFailure(final JavaScriptObject t) {

    }

    public JsValueCallbackWrapper() {
        super();

    }

}
