package io.nextweb.promise.js.callbacks;

import delight.async.callbacks.ValueCallback;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptObject;

import io.nextweb.promise.Fn;
import io.nextweb.promise.js.exceptions.ExceptionUtils;

/**
 * <p>
 * Used to call callbacks as required by async.js such as:
 * 
 * <pre>
 * function(ex, value) { ... }
 * </pre>
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public final class SimpleJavaScriptCallbackWrapper implements ValueCallback<Object> {

    private final JavaScriptObject callback;

    @Override
    public void onFailure(final Throwable t) {
        callCallback(callback, ExceptionUtils.convertToJSExceptionResult(Fn.exception(this, t)), null);
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

    public SimpleJavaScriptCallbackWrapper(final JavaScriptObject callback) {
        super();
        this.callback = callback;
    }

}
