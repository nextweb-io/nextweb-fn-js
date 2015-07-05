package io.nextweb.promise.js.callbacks;

import com.google.gwt.core.client.JavaScriptObject;

import delight.async.callbacks.ValueCallback;
import io.nextweb.promise.Fn;
import io.nextweb.promise.js.exceptions.ExceptionUtils;
import io.nextweb.promise.js.wrapping.JsWrap;
import io.nextweb.promise.js.wrapping.WrapperCollection;

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
public class JavaScriptCallbackWrapper implements ValueCallback<Object> {

    private final WrapperCollection wrappers;
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
        callCallback(callback, null, JsWrap.forcewrapAnyObjectForJavaScript(value, wrappers));

    }

    private final native void callCallback(JavaScriptObject cb, JavaScriptObject ex, Object value)/*-{
                                                                                                  cb(ex, value);
                                                                                                  }-*/;

    public JavaScriptCallbackWrapper(final WrapperCollection wrappers, final JavaScriptObject callback) {
        super();
        this.wrappers = wrappers;
        this.callback = callback;
    }

}
