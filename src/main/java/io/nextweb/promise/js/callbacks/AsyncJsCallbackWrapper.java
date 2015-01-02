package io.nextweb.promise.js.callbacks;

import io.nextweb.promise.Fn;
import io.nextweb.promise.js.exceptions.ExceptionUtils;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.callbacks.ValueCallback;

public class AsyncJsCallbackWrapper implements ValueCallback<Object> {

    private final JavaScriptObject callback;

    @Override
    public void onFailure(final Throwable t) {
        callCallback(callback, ExceptionUtils.wrapExceptionResult(Fn.exception(this, t)), null);
    }

    @Override
    public void onSuccess(final Object value) {

        if (value == null) {
            callCallback(callback, null, null);
            return;
        }
        callCallback(callback, null, ExporterUtil.wrap(value));

    }

    private final native void callCallback(JavaScriptObject cb, JavaScriptObject ex, Object value)/*-{
                                                                                                  cb(ex, value);
                                                                                                  }-*/;

    public AsyncJsCallbackWrapper(final JavaScriptObject callback) {
        super();
        this.callback = callback;
    }

}
