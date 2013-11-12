package io.nextweb.fn.js.wrapping;

import io.nextweb.fn.js.types.JsAtomicTypeWrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptObject;

public class WrapperCollection {

	private final List<Wrapper> registeredWrappers;

	public void addWrapper(final Wrapper wrapper) {
		registeredWrappers.add(wrapper);
	}

	public Object unwrapValueObjectForJava(final Object jsNode) {

		if (jsNode instanceof String) {
			return jsNode;
		}

		if (jsNode instanceof Integer) {
			return jsNode;
		}

		if (jsNode instanceof Boolean) {
			return jsNode;
		}

		if (jsNode instanceof Short) {
			return ((Short) jsNode).intValue();
		}

		if (jsNode instanceof Long) {
			return (int) ((Long) jsNode).longValue();
		}

		if (jsNode instanceof Byte) {
			return ((Byte) jsNode).intValue();
		}

		if (jsNode instanceof Character) {
			return jsNode;
		}

		if (jsNode instanceof Double) {
			if (Math.round(((Double) jsNode).floatValue()) == ((Double) jsNode)
					.floatValue()) {
				return new Long(Math.round(((Double) jsNode).doubleValue()));
			}
			return jsNode;
		}

		if (jsNode instanceof Float) {
			if (Math.round(((Float) jsNode).floatValue()) == ((Float) jsNode)
					.floatValue()) {
				return new Integer(Math.round(((Float) jsNode).floatValue()));
			}
			return jsNode;
		}

		if (jsNode instanceof Date) {
			return jsNode;
		}

		final Object obj = ExporterUtil.gwtInstance(jsNode);

		if (obj instanceof JavaScriptObject) {
			final JavaScriptObject jsobj = (JavaScriptObject) obj;
			if (JsWrap.isDate(jsobj)) {

				return JsWrap.dateFromJsDate(jsobj);
			}

			final JsAtomicTypeWrapper wrapper = (jsobj).cast();

			if (wrapper.isWrapper()) {
				return wrapper.getValue();
			}

		}

		for (final Wrapper wrapper : registeredWrappers) {
			if (wrapper.canUnwrap(obj)) {
				final Object unwrapped = wrapper.unwrap(obj);

				return unwrapped;
			}
		}
		// TODO replace!!!

		return obj;

	}

	// public final native static JavaScriptObject getJsObj(Object o)/*-{
	// return o;
	// }-*/;

	public Object convertValueObjectForJs(final Object gwtNode) {

		if (gwtNode instanceof String) {
			return gwtNode;
		}

		if (gwtNode instanceof Integer) {
			return gwtNode;
		}

		if (gwtNode instanceof Short) {
			return gwtNode;
		}

		if (gwtNode instanceof Long) {
			return gwtNode;
		}

		if (gwtNode instanceof Byte) {
			return gwtNode;
		}

		if (gwtNode instanceof Character) {
			return gwtNode;
		}

		if (gwtNode instanceof Double) {
			return gwtNode;
		}

		if (gwtNode instanceof Float) {

			return gwtNode;
		}

		if (gwtNode instanceof Boolean) {
			return gwtNode;
		}

		if (gwtNode instanceof Date) {
			return JsWrap.jsDateFromDate(((Date) gwtNode).getTime());
		}

		for (final Wrapper wrapper : registeredWrappers) {
			if (wrapper.canWrap(gwtNode)) {
				Object wrapped = wrapper.wrap(gwtNode);
				if (!(wrapped instanceof JavaScriptObject)) {
					wrapped = ExporterUtil.wrap(wrapped);
				}
				return wrapped;
			}
		}

		return ExporterUtil.wrap(gwtNode);
	}

	public WrapperCollection(
			List<Wrapper> wrappers) {
		super();
		this.registeredWrappers = new ArrayList<Wrapper>(wrappers);

	}
}
