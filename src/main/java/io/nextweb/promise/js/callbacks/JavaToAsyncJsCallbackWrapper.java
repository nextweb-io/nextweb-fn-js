package io.nextweb.promise.js.callbacks;

import com.google.gwt.core.client.JavaScriptObject;

public class JavaToAsyncJsCallbackWrapper {

    private native final JavaScriptObject createCb()/*-{
                                                    return function(ex, result) {
                                                    
                                                    };
                                                    }-*/;

}
