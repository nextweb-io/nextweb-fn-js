package io.nextweb.promise.js.wrapping;

import delight.functional.Closure;
import delight.functional.Function;

import java.util.Date;
import java.util.List;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptObject;

import io.nextweb.promise.js.FnJs;
import io.nextweb.promise.js.JsClosure;
import io.nextweb.promise.js.types.JsBasicType;

public final class JsWrap {

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
    public static Object unwrapValueObjectForJava(final Object jsNode, final WrapperCollection wrappers) {
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
    public static final JavaScriptObject forcewrapAnyObjectForJavaScript(final Object node,
            final WrapperCollection wrappers) {

        if (node instanceof JavaScriptObject) {
            return (JavaScriptObject) node;
        }

        if (FnJs.isBasicJsType(node)) {
            return ExporterUtil.wrap(JsBasicType.wrap(node));
        }

        final Object convertValueObjectForJs = wrappers.convertValueObjectForJs(node);

        if (convertValueObjectForJs instanceof JavaScriptObject) {
            return (JavaScriptObject) convertValueObjectForJs;
        }

        return ExporterUtil.wrap(convertValueObjectForJs);

    }

    public static <Type extends Object> Closure<Type> wrapJsClosure(final JsClosure closure,
            final WrapperCollection wrappers) {
        return new Closure<Type>() {

            @Override
            public void apply(final Type o) {
                closure.apply(JsWrap.unwrapBasicType(forcewrapAnyObjectForJavaScript(o, wrappers)));

            }
        };
    }

    public static <T> Closure<T> wrapJsClosure(final JsClosure closure, final Function<Object, Object> wrapper) {
        return new Closure<T>() {

            @Override
            public void apply(final T o) {
                closure.apply(wrapper.apply(o));

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
    public static final JavaScriptObject[] toJsoArray(final Object[] array, final WrapperCollection wrappers) {
        final JavaScriptObject[] result = new JavaScriptObject[array.length];
        for (int i = 0; i <= array.length - 1; i++) {
            final Object rawWrapped = forcewrapAnyObjectForJavaScript(array[i], wrappers);
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

    public final static native JavaScriptObject unwrapBasicTypes(JavaScriptObject jsArray)/*-{
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

    public static JavaScriptObject[] createJsList(final List<?> list, final Function<Object, Object> wrapper) {
        final JavaScriptObject[] result = new JavaScriptObject[list.size()];
        int count = 0;
        for (final Object o : list) {

            result[count] = (JavaScriptObject) wrapper.apply(o);
            count++;
        }
        return result;
    }

    public static Function<Object, Object> noWrapping() {
        return new Function<Object, Object>() {

            @Override
            public Object apply(final Object input) {

                return input;
            }
        };
    }

    public static Function<Object, Object> justExport() {
        return new Function<Object, Object>() {

            @Override
            public Object apply(final Object input) {

                return ExporterUtil.wrap(input);
            }
        };
    }

}
