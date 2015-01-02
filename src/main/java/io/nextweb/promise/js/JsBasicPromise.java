package io.nextweb.promise.js;

import io.nextweb.promise.js.exceptions.JsExceptionListeners;

import org.timepedia.exporter.client.Exportable;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Basic interface for all Js Result types.
 * 
 * @author Max
 * 
 * @param <JsResultType>
 * @param <ResultType>
 */
public interface JsBasicPromise<JsResultType extends JsBasicPromise<?>> extends JsExceptionListeners<JsResultType>,
        Exportable {

    public Object get(final Object... params);

    /**
     * <p>
     * Returns a function with the signature:
     * <code>function(ex, result) {.. }</code>
     * <p>
     * This is useful for using this with procedural asynchronous frameworks
     * such as async.js.
     * 
     * @return
     */
    public JavaScriptObject asFunction();

}
