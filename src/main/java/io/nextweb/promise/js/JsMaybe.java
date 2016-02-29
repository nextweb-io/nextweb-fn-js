package io.nextweb.promise.js;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

/**
 * <p>
 * Presents the potential for a value.
 *
 */
@Export
public class JsMaybe implements Exportable {

    private Object value;

    /**
     * If this object has a value.
     * 
     * @return <code>true</code> if this object has a value, <code>null</code>
     *         otherwise.
     */
    @Export
    public Object is() {
        if (value != null) {
            return true;
        } else {
            return null;
        }
    }

    /**
     * <p>
     * The value of this object.
     * 
     * @return The value of the object and <code>null</code> if this object does
     *         not have a value.
     */
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
