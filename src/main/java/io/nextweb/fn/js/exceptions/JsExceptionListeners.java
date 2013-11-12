package io.nextweb.fn.js.exceptions;

import io.nextweb.fn.js.JsClosure;

import org.timepedia.exporter.client.Exportable;

public interface JsExceptionListeners<ReturnType extends Exportable> {
	public ReturnType catchExceptions(final JsClosure exceptionListener);

	public ReturnType catchUndefined(final JsClosure undefinedListener);

	public ReturnType catchUnauthorized(final JsClosure unauthorizedListener);

	public ReturnType catchImpossible(final JsClosure impossibleListener);
}
