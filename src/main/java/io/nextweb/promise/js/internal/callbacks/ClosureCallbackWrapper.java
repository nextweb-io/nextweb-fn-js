package io.nextweb.promise.js.internal.callbacks;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.fn.Closure;

public class ClosureCallbackWrapper {

    private final Closure<Object> closure;

    public JavaScriptObject getJavaScriptCallback() {
        return createCallback();
    }

    private void callCallback(final Object param) {
        closure.apply(param);
    }

    private native JavaScriptObject createCallback()/*-{ 
                                                    
                                                    var self = this;
                                                    
                                                    var callbackFn = $entry(function(param) {
                                                    self.@io.nextweb.promise.js.internal.callbacks.ClosureCallbackWrapper::callCallback(Ljava/lang/Object;)(param);
                                                    });
                                                    
                                                    
                                                    return callbackFn;
                                                    
                                                    }-*/;

    public ClosureCallbackWrapper(final Closure<Object> closure) {
        super();
        this.closure = closure;

    }

}
