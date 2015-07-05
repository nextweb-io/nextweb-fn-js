package io.nextweb.promise.js;

import org.timepedia.exporter.client.Exportable;

import io.nextweb.promise.js.exceptions.JsExceptionListeners;

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

}
