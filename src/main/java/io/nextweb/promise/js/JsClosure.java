package io.nextweb.promise.js;

import delight.functional.Closure;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;

/**
 * <p>
 * A basic JavaScript callback, which expects one argument. For instance:
 * 
 * If a method expects a closure as a parameter, such as:
 * 
 * <pre>
 * object get(Closure callback);
 * </pre>
 * 
 * The closure object can be defined as follows:
 * 
 * <pre>
 * var cl = function(arg) {
 *    ...
 * });
 * get(cl);
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
