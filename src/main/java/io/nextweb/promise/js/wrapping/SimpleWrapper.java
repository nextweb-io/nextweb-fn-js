package io.nextweb.promise.js.wrapping;

import io.nextweb.promise.js.JsWrapper;


public abstract class SimpleWrapper implements Wrapper {

	@Override
	public boolean canUnwrap(Object input) {
		return input instanceof JsWrapper<?>;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object unwrap(Object input) {

		return ((JsWrapper<Object>) input).getOriginal();
	}

}