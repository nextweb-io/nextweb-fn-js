package io.nextweb.fn.js.internal.callbacks;

import io.nextweb.fn.Closure;
import io.nextweb.fn.js.wrapping.WrapperCollection;

import com.google.gwt.core.client.JavaScriptObject;

public class ClosureCallbackWrapper {

	private final Closure<Object> closure;
	private final WrapperCollection wrappers;

	public JavaScriptObject getJavaScriptCallback() {
		return null;
	}

	private void callCallback(Object param) {

	}

	private native JavaScriptObject createCallback()/*-{ 
													return function() {
													var self = this;
													var callbackFn = $entry(function(param) {
													self.@io.nextweb.fn.js.internal.callbacks.ClosureCallbackWrapper::callCallback(*)(param);
													});
													};
													return callbackFn;
													};
													}-*/;

	public ClosureCallbackWrapper(Closure<Object> closure,
			WrapperCollection wrappers) {
		super();
		this.closure = closure;
		this.wrappers = wrappers;
	}

}
