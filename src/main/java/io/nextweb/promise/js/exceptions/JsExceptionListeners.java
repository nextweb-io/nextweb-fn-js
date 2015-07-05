package io.nextweb.promise.js.exceptions;

import org.timepedia.exporter.client.Exportable;

import io.nextweb.promise.js.JsClosure;

public interface JsExceptionListeners<ReturnType extends Exportable> {
	public ReturnType catchExceptions(final JsClosure exceptionListener);

	public ReturnType catchUndefined(final JsClosure undefinedListener);

	public ReturnType catchUnauthorized(final JsClosure unauthorizedListener);

	public ReturnType catchImpossible(final JsClosure impossibleListener);
}
