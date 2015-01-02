package io.nextweb.promise.js.callbacks;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.callbacks.ValueCallback;

public class JavaToAsyncJsCallbackWrapper {

    public static JavaScriptObject wrap(final ValueCallback<Object> cb) {
        final JavaScriptObject jsValueCallback = JsValueCallbackWrapper.wrap(cb);

        return createCb(jsValueCallback);
    }

    private native static final JavaScriptObject createCb(JavaScriptObject cb)/*-{
                                                                              return function(ex, result) {
                                                                              if (ex) {
                                                                              cb.onFailure(ex);
                                                                              return;
                                                                              }

                                                                              cb.onSuccess(result);
                                                                              };
                                                                              }-*/;

}
