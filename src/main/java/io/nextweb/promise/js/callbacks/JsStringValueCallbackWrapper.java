package io.nextweb.promise.js.callbacks;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExporterUtil;
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

    @NoExport
    public static final JavaScriptObject wrap(final ValueCallback<String> callback) {
        final JsStringValueCallbackWrapper wrapper = new JsStringValueCallbackWrapper();
        wrapper.wrapped = callback;
        return ExporterUtil.wrap(wrapper);
    }

    @Export
    public void onFailure(final JavaScriptObject t) {
        final Object gwtInstance = ExporterUtil.gwtInstance(t);

        if (gwtInstance instanceof Throwable) {
            wrapped.onFailure((Throwable) gwtInstance);
            return;
        }

        if (gwtInstance instanceof String) {
            wrapped.onFailure(new Exception((String) gwtInstance));
            return;
        }

        wrapped.onFailure(new Exception("Failure while processing native JavaScript function."));
    }

    public JsStringValueCallbackWrapper() {
        super();

    }

}