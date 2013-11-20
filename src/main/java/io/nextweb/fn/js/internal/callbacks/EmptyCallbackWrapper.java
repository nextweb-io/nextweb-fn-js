package io.nextweb.fn.js.internal.callbacks;

import io.nextweb.fn.js.callbacks.EmptyCallback;

import com.google.gwt.core.client.JavaScriptObject;

public class EmptyCallbackWrapper {

	private final EmptyCallback callback;

	private void callCallback() {
		callback.call();
	};
	
	public JavaScriptObject getJavaScriptCallback() {
		return createCallback();
	}

	private native JavaScriptObject createCallback()/*-{ 
													
													return function() {
													var self = this;
													var callbackFn = $entry(function(val) {
													self.@io.nextweb.fn.js.internal.callbacks.callCallback()();
													});
													};
													return callbackFn;
													}-*/;

	public EmptyCallbackWrapper(EmptyCallback callback) {
		super();
		assert callback != null;
		this.callback = callback;
	}

}
