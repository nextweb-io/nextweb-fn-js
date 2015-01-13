package io.nextweb.promise.js.callbacks;

import io.nextweb.promise.Fn;
import io.nextweb.promise.js.exceptions.ExceptionUtils;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.callbacks.ValueCallback;

/**
 * Trigger JS callbacks without converting object to their exported form.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public class UnsafeJavaScriptCallbackWrapper implements ValueCallback<Object> {

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
        callCallback(callback, null, value);

    }

    private final native void callCallback(JavaScriptObject cb, JavaScriptObject ex, Object value)/*-{
                                                                                                  cb(ex, value);
                                                                                                  }-*/;

    public UnsafeJavaScriptCallbackWrapper(final JavaScriptObject callback) {
        super();

        this.callback = callback;
    }

}
