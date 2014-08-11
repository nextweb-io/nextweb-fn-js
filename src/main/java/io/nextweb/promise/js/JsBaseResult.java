package io.nextweb.promise.js;

import io.nextweb.promise.js.exceptions.JsExceptionListeners;

import org.timepedia.exporter.client.Exportable;

/**
 * Basic interface for all Js Result types.
 * 
 * @author Max
 * 
 * @param <JsResultType>
 * @param <ResultType>
 */
public interface JsBaseResult<JsResultType extends JsBaseResult<?>> extends
		JsExceptionListeners<JsResultType>, Exportable {

	public Object get(final Object... params);

}
