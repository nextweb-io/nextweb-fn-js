package io.nextweb.promise.js.callbacks;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.callbacks.ValueCallback;

@Export
public class JsStringValueCallbackWrapper {

    @NoExport
    ValueCallback<String> wrapped;

    @Export
    public void onSuccess(final String value) {
        wrapped.onSuccess(value);
    }

    @Export
    public void onFailure(final JavaScriptObject t) {

    }

    public JsStringValueCallbackWrapper() {
        super();

    }

}
