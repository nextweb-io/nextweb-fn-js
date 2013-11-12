package io.nextweb.fn.js.types;

import io.nextweb.fn.js.FnJs;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

import com.google.gwt.core.client.JavaScriptObject;

@Export
public class JsBasicType implements Exportable {

	private Object value;

	@Export
	public int intValue() {

		return ((Number) value).intValue();
	}

	@Export
	public String stringValue() {
		return (String) value;
	}

	@Export
	public JavaScriptObject booleanValue() {
		if (((Boolean) value).booleanValue() == true) {
			return booleanTrue();
		} else {
			return booleanFalse();
		}
	}

	private final native JavaScriptObject booleanTrue()/*-{
														var val = {
														value : new Boolean(true)
														};
														return val;
														}-*/;

	private final native JavaScriptObject booleanFalse()/*-{
														var val = {
														value : new Boolean(false)
														};
														return val;
														}-*/;

	@Export
	public double doubleValue() {

		return (Double) value;
	}

	@Export
	public int isString() {
		return FnJs.isJsString(value) ? 1 : 0;
	}

	@Export
	public int isInt() {
		return FnJs.isJsInteger(value) ? 1 : 0;
	}

	@Export
	public int isDouble() {
		return FnJs.isJsDouble(value) ? 1 : 0;
	}

	@Export
	public int isBoolean() {
		return value instanceof Boolean ? 1 : 0;
	}

	@Export
	public int isJsBasicType() {
		return 1;
	}

	@Export
	@Override
	public String toString() {
		return "wrapped[" + value + ", " + value.getClass() + "]";
	}

	public void setValue(final Object value) {
		this.value = value;
	}

	public static JsBasicType wrap(final Object node) {
		final JsBasicType jsBasicType = new JsBasicType();

		jsBasicType.setValue(node);
		assert jsBasicType.isString() != 0 || jsBasicType.isInt() != 0
				|| jsBasicType.isDouble() != 0 || jsBasicType.isBoolean() != 0 : "Attempting to wrap non-basic type.";

		return jsBasicType;
	}

	public JsBasicType() {
		super();
	}

}
