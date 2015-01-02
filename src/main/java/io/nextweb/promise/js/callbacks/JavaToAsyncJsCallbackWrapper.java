package io.nextweb.promise.js.callbacks;

import io.nextweb.promise.js.JsClosure;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.callbacks.ValueCallback;

public class JavaToAsyncJsCallbackWrapper {

    public static JavaScriptObject wrap(final ValueCallback<Object> cb) {
        final JavaScriptObject operation = new JsClosure() {

            @Override
            public void apply(final Object result) {

            }
        };

        return createCb(jsValueCallback);
    }

    private native static final JavaScriptObject createCb(JavaScriptObject operation)/*-{
                                                                                     return function(cb) {
                                                                                     operation.apply(cb);
                                                                                     };
                                                                                     }-*/;

}
