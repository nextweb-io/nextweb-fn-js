package io.nextweb.promise.js.callbacks;

import io.nextweb.promise.NextwebPromise;
import io.nextweb.promise.js.JsClosure;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.callbacks.ValueCallback;

public class PromiseToAsyncJsOperationWrapper {

    public static <T> JavaScriptObject wrap(final NextwebPromise<T> promise) {
        final JavaScriptObject operation = ExporterUtil.wrap(new JsClosure() {

            @Override
            public void apply(final Object result) {
                final AsyncJsToJavaCallbackWrapper callback = new AsyncJsToJavaCallbackWrapper(ExporterUtil
                        .wrap(result));

                promise.apply(new ValueCallback<T>() {

                    @Override
                    public void onFailure(final Throwable t) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onSuccess(final T value) {
                        callback.onSuccess(value);
                    }
                });

            }
        });

        return createCb(operation);
    }

    private native static final JavaScriptObject createCb(JavaScriptObject operation)/*-{
                                                                                     return function(cb) {
                                                                                     operation.apply(cb);
                                                                                     };
                                                                                     }-*/;

}
