package io.nextweb.promise.js.callbacks;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.ExporterUtil;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.callbacks.ValueCallback;

@Export
public class JsValueCallbackWrapper implements Exportable {

    @NoExport
    ValueCallback<Object> wrapped;

    @Export
    public void onSuccess(final String value) {
        wrapped.onSuccess(ExporterUtil.gwtInstance(value));
    }

    @NoExport
    public static final JavaScriptObject wrap(final ValueCallback<Object> callback) {
        final JsValueCallbackWrapper wrapper = new JsValueCallbackWrapper();
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

        wrapped.onFailure(new Exception("Failure while processing native JavaScript exception: " + t));
    }

    public JsValueCallbackWrapper() {
        super();

    }

}
