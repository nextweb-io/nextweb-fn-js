package io.nextweb.promise.js;

import delight.functional.Function;
import delight.functional.Success;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.ExporterUtil;
import org.timepedia.exporter.client.NoExport;

/**
 * <p>
 * A simple dummy object, which is returned by operations to signify that they
 * have been successful.
 * <p>
 * This object has no methods or attributes.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
@Export
public class JsSuccess implements Exportable {

    @Export
    public void isSuccess() {

    }

    public JsSuccess() {
        super();
    }

    @NoExport
    public static Function<Object, Object> getWrapper() {
        return new Function<Object, Object>() {

            @Override
            public Object apply(final Object input) {

                if (!(input instanceof Success)) {
                    throw new IllegalArgumentException(
                            "Result of type Success expected. Instead received: " + input.getClass());
                }

                return ExporterUtil.wrap(new JsSuccess());
            }
        };
    }

}
