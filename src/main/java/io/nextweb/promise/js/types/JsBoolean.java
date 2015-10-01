package io.nextweb.promise.js.types;

import delight.functional.Function;

import org.timepedia.exporter.client.ExporterUtil;

import io.nextweb.promise.js.wrapping.JsWrap;

public class JsBoolean {

    public static Function<Object, Object> getWrapper() {
        return new Function<Object, Object>() {

            @Override
            public Object apply(final Object input) {
                final Boolean value = (Boolean) input;

                return JsWrap.unwrapBasicType(ExporterUtil.wrap(JsBasicType.wrap(value)));
            }

        };
    }

}
