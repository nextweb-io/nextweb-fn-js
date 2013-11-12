package io.nextweb.fn.js.wrapping;

import io.nextweb.fn.js.JsWrapper;


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