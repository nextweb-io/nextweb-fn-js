package io.nextweb.promise.js.exceptions;

import delight.gwt.console.Console;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.UmbrellaException;

import io.nextweb.promise.exceptions.ExceptionResult;

public final class ExceptionUtils {

    public static final Throwable convertToJavaException(final Object jsException) {
        final Object obj = ExporterUtil.gwtInstance(jsException);

        if (obj instanceof JsExportedException) {

            final JsExportedException exportedException = (JsExportedException) obj;
            return exportedException.original.exception();
        }

        if (obj instanceof Throwable) {
            return (Throwable) obj;
        }

        return new Exception(
                "Cannot convert reported exception result to Java Exception.\n" + "  Exception Result Type: "
                        + obj.getClass() + "\n" + "  Exception Result toString: " + obj.toString());
    }

    public static final JavaScriptObject convertToJSExceptionResult(final ExceptionResult er) {
        Console.log("Create jsx " + er.exception().getMessage());

        final JsExportedException jsex = new JsExportedException();
        jsex.origin = er.origin().toString();
        jsex.exception = er.exception().getMessage();
        jsex.message = er.exception().getMessage();
        jsex.origintrace = getOriginTrace();
        jsex.stacktrace = getStacktrace(er.exception());
        jsex.stack = jsex.stacktrace;
        jsex.jsException = getJsException(er.exception());
        jsex.original = er;

        Console.log("Created jsx " + jsex.message);

        return ExporterUtil.wrap(jsex);
    }

    private static native final String attemptToGetMessage(
            JavaScriptObject exception)/*-{ 
                                       
                                       if (exception.message) {
                                       return exception.message;
                                       }
                                       
                                       return null;
                                       }-*/;

    private static final native void triggerFunction(JavaScriptObject func, JavaScriptObject arg) /*-{
                                                                                                  func(arg);
                                                                                                  }-*/;

    /*-{
                                                                                function Exception() {
                                                                                                          
                                                                                                          }
                                                                                                          
                                                                                                          Exception.prototype.toString = function() {return "Exception: "+exceptionMessage;  };
                                                                                                          var res = new Exception();
                                                                                                          res.message = exceptionMessage;                                      
                                                                                                          res.exception =exceptionMessage;
                                                                                                          res.origin = origin;
                                                                                                          res.origintrace = originTrace;
                                                                                                          res.stacktrace = stacktrace;
                                                                                                          res.stack = stacktrace;
                                                                                                          res.jsException = jsException;
                                                                                                          return res;
                                                                                }-*/;

    private final static JavaScriptObject getJsException(final Throwable ex) {
        final Throwable uw = unwrap(ex);
        if (!(uw instanceof JavaScriptException)) {
            return null;
        }

        return ((JavaScriptException) uw).getException();

    }

    public static final String getStacktrace(final Throwable r) {
        try {
            if (r == null) {
                return "null";
                // throw new
                // IllegalArgumentException("Cannot create stacktrace for
                // null.");
            }
            final Throwable unwrapped = unwrap(r);
            String stacktrace;
            try {
                stacktrace = unwrapped.toString() + "<br />\n";
            } catch (final Throwable t) {
                stacktrace = "Error creating stacktrace for " + r.getMessage() + ".\n  Exception reported: "
                        + t.getMessage();

            }

            if (unwrapped instanceof JavaScriptException) {
                final JavaScriptException jsException = (JavaScriptException) unwrapped;
                if (jsException.getException() != null) {
                    stacktrace += "JavaScriptException:<br/>\n"
                            + getJavaScriptExceptionStackTrace(jsException.getException()).replaceAll("\n", "<br/>\n")
                            + "<br/>\n-- End of JavaScriptException";
                }

            }

            for (final StackTraceElement element : unwrapped.getStackTrace()) {
                stacktrace += element + "<br/>\n";
            }

            stacktrace += getCauseTrace(unwrap(r));
            return stacktrace;
        } catch (final Throwable t) {
            return "Error creating stacktrace: " + t.getMessage();
        }
    }

    private static native final String getJavaScriptExceptionStackTrace(JavaScriptObject ex)/*-{
                                                                                            if (!ex.stack) {
                                                                                            return "JavaScriptException stack trace not available for this browser";
                                                                                            }
                                                                                            return ex.stack;
                                                                                            }-*/;

    private static final String getOriginTrace() {
        try {
            throw new Exception("Origin");
        } catch (final Throwable t) {
            return getStacktrace(t);
        }

    }

    private static final Throwable unwrap(final Throwable e) {
        if (e instanceof UmbrellaException) {
            final UmbrellaException ue = (UmbrellaException) e;
            if (ue.getCauses().size() == 1) {
                return unwrap(ue.getCauses().iterator().next());
            }
        }
        return e;
    }

    private static final String getCauseTrace(final Throwable t) {
        if (t.getCause() == null) {
            return "-end of stack trace";
        }

        String res = "Caused By: " + unwrap(t.getCause()).toString() + "<br/>\n";
        for (final StackTraceElement element : unwrap(t.getCause()).getStackTrace()) {
            res += element + "<br/>\n";
        }

        return res;
    }

}
