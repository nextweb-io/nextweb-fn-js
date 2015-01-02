package io.nextweb.promise.js.callbacks;

import io.nextweb.promise.exceptions.ExceptionListener;
import io.nextweb.promise.exceptions.ExceptionResult;
import io.nextweb.promise.js.JsClosure;
import io.nextweb.promise.js.JsNextwebPromise;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.fn.Closure;

public class PromiseToAsyncJsOperationWrapper {

    public static <T, R> JavaScriptObject wrap(final JsNextwebPromise<T, R> promise) {
        final JavaScriptObject operation = ExporterUtil.wrap(new JsClosure() {

            @Override
            public void apply(final Object result) {
                final AsyncJsToJavaCallbackWrapper callback = new AsyncJsToJavaCallbackWrapper(ExporterUtil
                        .wrap(result));

                promise.catchExceptions(new ExceptionListener() {

                    @Override
                    public void onFailure(final ExceptionResult r) {
                        callback.onFailure(r.exception());
                    }
                });

                promise.get(new Closure<V>() {

                    @Override
                    public void apply(final V o) {
                        callback.onSuccess(o);
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
