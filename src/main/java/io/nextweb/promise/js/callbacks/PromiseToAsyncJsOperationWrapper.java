package io.nextweb.promise.js.callbacks;

import delight.async.Operation;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptObject;

import io.nextweb.promise.js.wrapping.WrapperCollection;

public class PromiseToAsyncJsOperationWrapper {

    public static <T> JavaScriptObject wrapSafe(final WrapperCollection wrappers, final Operation<T> operation) {
        final JavaScriptObject jsOperation = ExporterUtil.wrap(JsObjectClosure.wrap(new Closure<Object>() {

            @Override
            public void apply(final Object result) {

                final JavaScriptCallbackWrapper callback = new JavaScriptCallbackWrapper(wrappers, ExporterUtil
                        .wrap(result));

                operation.apply(new ValueCallback<T>() {

                    @Override
                    public void onFailure(final Throwable t) {
                        callback.onFailure(t);
                    }

                    @Override
                    public void onSuccess(final T value) {
                        callback.onSuccess(value);
                    }
                });

            }
        }));

        return createCb(jsOperation);
    }

    private native static final JavaScriptObject createCb(JavaScriptObject operation)/*-{
                                                                                     return function(cb) {
                                                                                     operation.apply(cb);
                                                                                     };
                                                                                     }-*/;

}
