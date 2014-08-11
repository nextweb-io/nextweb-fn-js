package io.nextweb.promise.js;

import io.nextweb.promise.js.callbacks.EmptyCallback;
import io.nextweb.promise.js.internal.callbacks.ClosureCallbackWrapper;
import io.nextweb.promise.js.internal.callbacks.EmptyCallbackWrapper;
import io.nextweb.promise.js.types.JsArray;
import io.nextweb.promise.js.wrapping.JsWrap;
import io.nextweb.promise.js.wrapping.WrapperCollection;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.fn.Closure;

public class FnJs {

	public static final boolean isJsString(final Object value) {
	    return value instanceof String;
	}

	public static final boolean isJsInteger(final Object value) {
	    return value instanceof Integer || value instanceof Short
	            || value instanceof Long || value instanceof Byte;
	}

	public static final boolean isJsDouble(final Object value) {
	    return value instanceof Float || value instanceof Double;
	}

	public static final boolean isJsBoolean(final Object value) {
	    return value instanceof Boolean;
	}

	public static final boolean isBasicJsType(final Object node) {
	    return isJsString(node) || isJsInteger(node) || isJsDouble(node)
	            || isJsBoolean(node);
	}

	public static final native void triggerCallbackJs(JavaScriptObject fn,
	JavaScriptObject jsArray)/*-{
	                         fn.apply(this, jsArray.getArray());
	                         }-*/;

	public static final JsClosure asJsClosure(final JavaScriptObject fn,
	        final WrapperCollection wrappers) {
	
	    return new JsClosure() {
	
	        @Override
	        public void apply(final Object result) {
	
	            assert result instanceof JavaScriptObject;
	
	            triggerCallback(fn, wrappers, new Object[] { result });
	
	        }
	    };
	
	}

	public static final void triggerCallback(final JavaScriptObject fn,
	        final WrapperCollection wrappers, final Object[] params) {
	    triggerCallbackJs(fn, ExporterUtil.wrap(JsArray.wrap(JsWrap
	            .toJsoArray(params, wrappers))));
	}
	
	public static final JavaScriptObject exportCallback(EmptyCallback callback) {
		EmptyCallbackWrapper wrapper = new EmptyCallbackWrapper(callback);
		return wrapper.getJavaScriptCallback();
		
	}
	
	public static final JavaScriptObject exportCallback(Closure<Object> closure) {
		ClosureCallbackWrapper wrapper = new ClosureCallbackWrapper(closure);
		return wrapper.getJavaScriptCallback();
	}
	
}