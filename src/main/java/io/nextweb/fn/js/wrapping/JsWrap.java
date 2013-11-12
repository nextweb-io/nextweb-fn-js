package io.nextweb.fn.js.wrapping;


import io.nextweb.fn.Closure;
import io.nextweb.fn.js.FnJs;
import io.nextweb.fn.js.JsClosure;
import io.nextweb.fn.js.types.JsBasicType;


import java.util.Date;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptObject;

public class JsWrap {

	public final native static String timeFromJsDate(final JavaScriptObject d)/*-{
	return "t" + d.getTime();
	}-*/;

	public final static native JavaScriptObject jsDateFromDate(double time)/*-{
	return new Date(time);
	}-*/;

	public final native static boolean isDate(final JavaScriptObject d)/*-{
	return (d && d.getTime && typeof d.getTime == 'function') ? true
	: false;
	}-*/;

	public final static Date dateFromJsDate(final JavaScriptObject d) {
		final String dateStr = timeFromJsDate(d);
	
		final long time = Long.valueOf(dateStr.substring(1));
	
		final Date date = new Date(time);
	
		return date;
	}

	public final native static JavaScriptObject getJsObject(final String json)/*-{
	return eval("(" + json + ")");
	}-*/;

	/**
	 * Prepare a node passed from JavaScript to be processed by the Nextweb Java
	 * API.
	 * 
	 * @param jsNode
	 * @param wrappers
	 * @return
	 */
	public static Object unwrapValueObjectForJava(final Object jsNode,
			final WrapperCollection wrappers) {
		return wrappers.unwrapValueObjectForJava(jsNode);
	}

	/**
	 * Will wrap all objects including Strings, Integers etc into
	 * JavaScriptObjects. Basic JS types (such as String) will be placed in a
	 * special wrapper Object JsBasicType.
	 * 
	 * @param node
	 * @return
	 */
	public static final JavaScriptObject forcewrapAnyObjectForJavaScript(
			final Object node, final WrapperCollection wrappers) {
	
		if (node instanceof JavaScriptObject) {
			return (JavaScriptObject) node;
		}
	
		if (FnJs.isBasicJsType(node)) {
			return ExporterUtil.wrap(JsBasicType.wrap(node));
		}
	
		final Object convertValueObjectForJs = wrappers
				.convertValueObjectForJs(node);
		if (convertValueObjectForJs instanceof JavaScriptObject) {
			return (JavaScriptObject) convertValueObjectForJs;
		}
	
		return ExporterUtil.wrap(convertValueObjectForJs);
	
	}

	/**
	 * Supports wrapping both of Engine nodes and value nodes.</br> This
	 * function will <b>keep</b> basic types like Integer/Boolean/etc.
	 * 
	 * @param javaNode
	 * @param wrappers
	 * @return
	 */
	// public static Object wrapAnyObjectForJavaScript(final Object javaNode,
	// final WrapperCollectionNextweb wrappers) {
	// return wrappers
	// .convertValueObjectForJs(createJsEngineWrapperIfPossible(
	// javaNode, wrappers.getFactory()));
	// }
	
	public static <Type extends Object> Closure<Type> wrapJsClosure(
			final JsClosure closure, final WrapperCollection wrappers) {
		return new Closure<Type>() {
	
			@Override
			public void apply(final Type o) {
				closure.apply(JsWrap
						.unwrapBasicType(forcewrapAnyObjectForJavaScript(o,
								wrappers)));
	
			}
		};
	}

	/**
	 * Only safe for Engine nodes (not for value nodes!)
	 * 
	 * @param array
	 * @param wrappers
	 * @return
	 */
	public static final JavaScriptObject[] toJsoArray(final Object[] array,
			final WrapperCollection wrappers) {
		final JavaScriptObject[] result = new JavaScriptObject[array.length];
		for (int i = 0; i <= array.length - 1; i++) {
			final Object rawWrapped = forcewrapAnyObjectForJavaScript(array[i],
					wrappers);
			if (rawWrapped instanceof JavaScriptObject) {
				result[i] = (JavaScriptObject) rawWrapped;
			} else {
				result[i] = ExporterUtil.wrap(rawWrapped);
			}
		}
		return result;
	}

	/**
	 * This works for all types but booleans
	 * 
	 * @param value
	 * @return
	 */
	public final static native Object unwrapBasicType(Object value)/*-{
																	
																	var result = value;
																	
																	if (result.isJsBasicType && typeof result.isJsBasicType === 'function') {
																	
																	if (result.isInt() == 1) {
																	return result.intValue();
																	}
																	if (result.isDouble() == 1) {
																	return result.doubleValue();
																	}
																	if (result.isString() == 1) {
																	return result.stringValue();
																	}
																	if (result.isBoolean() == 1) {
																	return result.booleanValue().value;
																	}
																	}
																	
																	return result;
																	
																	}-*/;

	public final static native JavaScriptObject unwrapBasicTypes(
	JavaScriptObject jsArray)/*-{
								var values = jsArray.getArray();
								
								for ( var i = 0; i <= values.length - 1; i++) {
								var value = values[i];
								if (value.isJsBasicType
								&& typeof value.isJsBasicType === 'function') {
								var rpl;
								if (value.isString() != 0) {
								rpl = value.stringValue();
								}
								
								if (value.isInt() != 0) {
								rpl = value.intValue();
								}
								
								if (value.isDouble() != 0) {
								rpl = value.doubleValue();
								}
								
								if (value.isBoolean() != 0) {
								rpl = value.booleanValue().value;
								}
								values[i] = rpl;
								}
								}
								
								return values;
								}-*/;

}
