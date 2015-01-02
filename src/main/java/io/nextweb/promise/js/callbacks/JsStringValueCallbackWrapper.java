package io.nextweb.promise.js.callbacks;

import io.nextweb.promise.js.exceptions.ExceptionUtils;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.ExporterUtil;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.callbacks.ValueCallback;

@Export
public class JsStringValueCallbackWrapper implements Exportable {

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
        wrapped.onFailure(ExceptionUtils.convertToJavaException(t));
    }

    public JsStringValueCallbackWrapper() {
        super();

    }

}
