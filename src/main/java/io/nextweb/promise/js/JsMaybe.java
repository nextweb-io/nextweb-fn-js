package io.nextweb.promise.js;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

/**
 * 
 * 
 *
 */
@Export
public class JsMaybe implements Exportable {

    private Object value;

    @Export
    public Object is() {
        if (value != null) {
            return "is";
        } else {
            return null;
        }
    }

    @Export
    public Object value() {
        return value;
    }

    @NoExport
    public static JsMaybe fromValue(final Object value) {
        final JsMaybe maybe = new JsMaybe();

        maybe.value = value;

        return maybe;
    }

    public JsMaybe() {
        super();
    }

}
