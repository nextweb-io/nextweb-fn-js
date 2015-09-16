package io.nextweb.promise.js.types;

import delight.functional.Function;

import org.timepedia.exporter.client.ExporterUtil;

public class JsLong {

    public static Function<Object, Object> getWrapper() {

        return new Function<Object, Object>() {

            @Override
            public Object apply(final Object input) {

                return ExporterUtil.wrap(((Long) input).intValue());
            }
        };

    }

}
