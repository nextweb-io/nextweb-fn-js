package io.nextweb.promise.js;

import delight.functional.Closure2;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;

@Export
@ExportClosure
public interface JsClosure2 extends Exportable, Closure2<Object, Object> {

    @Override
    void apply(Object p1, Object p2);

}
