package io.nextweb.promise.js;

import delight.functional.Closure;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;

/**
 * <p>
 * A basic JavaScript closure, which expects one argument. For instance:
 * 
 * <pre>
 * var cl = function(arg) {
 * 
 * });
 * </pre>
 * 
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
@Export
@ExportClosure
public interface JsClosure extends Exportable, Closure<Object> {

    @Override
    public void apply(Object result);

}
