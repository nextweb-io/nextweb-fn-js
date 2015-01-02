package io.nextweb.promise.js.callbacks;

import org.timepedia.exporter.client.ExporterUtil;

import io.nextweb.promise.js.JsClosure;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.callbacks.ValueCallback;

public class JavaToAsyncJsOperationWrapper {

    public static JavaScriptObject wrap(final ValueCallback<Object> cb) {
        final JavaScriptObject operation = ExporterUtil.wrap(new JsClosure() {

            @Override
            public void apply(final Object result) {
                   AsyncJsToJavaCallbackWrapper callback = new AsyncJsToJavaCallbackWrapper(ExporterUtil.wrap(result));
                   
                   
                   
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
