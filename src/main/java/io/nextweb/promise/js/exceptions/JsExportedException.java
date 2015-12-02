package io.nextweb.promise.js.exceptions;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.core.client.JavaScriptObject;

import io.nextweb.promise.exceptions.ExceptionResult;

@Export
public class JsExportedException implements Exportable {

    @Export
    public String message;

    @Export
    public String exception;

    @Export
    public String origin;

    @Export
    public String origintrace;

    @Export
    public String stacktrace;

    @Export
    public String stack;

    @Export
    public JavaScriptObject jsException;

    @NoExport
    public ExceptionResult original;

    public JsExportedException() {
        super();
    }

}
