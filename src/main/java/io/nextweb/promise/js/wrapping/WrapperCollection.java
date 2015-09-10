package io.nextweb.promise.js.wrapping;

import delight.functional.Function;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptObject;

import io.nextweb.promise.js.types.JsAtomicTypeWrapper;

public class WrapperCollection {

    private final List<Wrapper> registeredWrappers;

    public void addWrapper(final Wrapper wrapper) {
        registeredWrappers.add(wrapper);
    }

    public boolean hasWrapper(final Wrapper wrapper) {
        return registeredWrappers.contains(wrapper);
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
            if (Math.round(((Double) jsNode).floatValue()) == ((Double) jsNode).floatValue()) {
                return new Long(Math.round(((Double) jsNode).doubleValue()));
            }
            return jsNode;
        }

        if (jsNode instanceof Float) {
            if (Math.round(((Float) jsNode).floatValue()) == ((Float) jsNode).floatValue()) {
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

        if (gwtNode instanceof List<?>) {
            final List<?> list = (List<?>) gwtNode;

            final JavaScriptObject[] result = createJsList(list, new Function<Object, JavaScriptObject>() {

                @Override
                public JavaScriptObject apply(final Object input) {
                    return ExporterUtil.wrap(convertValueObjectForJs(input));
                }
            });

            return ExporterUtil.wrap(result);

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

    public static JavaScriptObject[] createJsList(final List<?> list,
            final Function<Object, JavaScriptObject> wrapper) {
        final JavaScriptObject[] result = new JavaScriptObject[list.size()];
        int count = 0;
        for (final Object o : list) {

            result[count] = wrapper.apply(o);
            count++;
        }
        return result;
    }

    public WrapperCollection(final List<Wrapper> wrappers) {
        super();
        this.registeredWrappers = new ArrayList<Wrapper>(wrappers);

    }
}
