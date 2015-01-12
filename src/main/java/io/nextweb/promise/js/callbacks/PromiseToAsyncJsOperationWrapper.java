package io.nextweb.promise.js.callbacks;

import io.nextweb.promise.js.Console;
import io.nextweb.promise.js.JsClosure;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.Operation;
import de.mxro.async.callbacks.ValueCallback;

public class PromiseToAsyncJsOperationWrapper {

    public static <T> JavaScriptObject wrap(final Operation<T> operation) {
        final JavaScriptObject jsOperation = ExporterUtil.wrap(new JsClosure() {

            @Override
            public void apply(final Object result) {

                Console.log("Applying ...");

                final JavaScriptCallbackWrapper callback = new JavaScriptCallbackWrapper(ExporterUtil.wrap(result));

                operation.apply(new ValueCallback<T>() {

                    @Override
                    public void onFailure(final Throwable t) {
                        callback.onFailure(t);
                    }

                    @Override
                    public void onSuccess(final T value) {
                        Console.log("rceived success");
                        callback.onSuccess(value);
                    }
                });

            }
        });
        Console.log(jsOperation);
        return createCb(jsOperation);
    }

    private native static final JavaScriptObject createCb(JavaScriptObject operation)/*-{
                                                                                     return function(cb) {
                                                                                     operation.apply(cb);
                                                                                     };
                                                                                     }-*/;

}
