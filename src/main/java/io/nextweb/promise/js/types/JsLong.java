package io.nextweb.promise.js.types;

import delight.functional.Function;

import org.timepedia.exporter.client.ExporterUtil;

import io.nextweb.promise.js.wrapping.JsWrap;

public class JsLong {

    public static Function<Object, Object> getWrapper() {

        return new Function<Object, Object>() {

            @Override
            public Object apply(final Object input) {

                final Integer value = ((Long) input).intValue();

                return JsWrap.unwrapBasicType(ExporterUtil.wrap(JsBasicType.wrap(value)));

            }
        };

    }

}
