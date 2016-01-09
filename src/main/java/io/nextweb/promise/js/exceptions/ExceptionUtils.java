package io.nextweb.promise.js.exceptions;

import delight.async.Value;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.UmbrellaException;

import io.nextweb.promise.exceptions.ExceptionResult;

public final class ExceptionUtils {

    public static final Throwable convertToJavaException(final Object jsException) {
        final Object obj = ExporterUtil.gwtInstance(jsException);

        if (jsException == null) {
            return new Exception("Trying to convert exception null to Java exception.");
        }

        if (obj instanceof JsExportedException) {

            final JsExportedException exportedException = (JsExportedException) obj;
            return exportedException.original.exception();
        }

        if (obj instanceof Throwable) {
            return (Throwable) obj;
        }

        final Value<Throwable> exVal = new Value<Throwable>(null);

        try {
            final UncaughtExceptionHandler oldHandler = GWT.getUncaughtExceptionHandler();
            GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

                @Override
                public void onUncaughtException(final Throwable e) {
                    exVal.set(e);
                }
            });
            triggerJsException(jsException);

            return new Exception(
                    "Cannot convert reported exception result to Java Exception.\n" + "  Exception Result Type: "
                            + obj.getClass() + "\n" + "  Exception Result toString: " + obj.toString());
        } catch (final Throwable t) {
            return t;
        }
    }

    private static native final void triggerJsException(Object jsException)/*-{
                                                                           if (jsException.message) {
                                                                           throw jsException;
                                                                           }
                                                                           if (jsException.original) {
                                                                           throw jsException.original;
                                                                           }
                                                                           if (jsException.jsException) {
                                                                             throw jsException.jsException;
                                                                           }
                                                                           }-*/;

    public static final JavaScriptObject convertToJSExceptionResult(final ExceptionResult er) {
        // Console.log("Create jsx " + er.exception().getMessage());

        final JsExportedException jsex = new JsExportedException();
        jsex.origin = er.origin().toString();
        jsex.exception = er.exception().getMessage();
        jsex.message = er.exception().getMessage();
        jsex.origintrace = getOriginTrace();
        jsex.stacktrace = getStacktraceAsHtml(er.exception());
        jsex.stack = jsex.stacktrace;
        jsex.jsException = getJsException(er.exception());
        jsex.original = er;

        // Console.log("Created jsx " + jsex.message);

        final JavaScriptObject exception = ExporterUtil.wrap(jsex);

        setProperty(exception, "origin", jsex.origin);
        setProperty(exception, "exception", jsex.exception);
        setProperty(exception, "message", jsex.message);
        setProperty(exception, "origintrace", jsex.origintrace);
        setProperty(exception, "stacktrace", jsex.stacktrace);
        setProperty(exception, "stack", jsex.stack);
        setProperty(exception, "jsException", jsex.jsException);

        return exception;
    }

    private static native final void setProperty(JavaScriptObject obj, String propName, String value)/*-{ 
                                                                                                     obj[propName] = value;                 
                                                                                                     }-*/;

    private static native final void setProperty(JavaScriptObject obj, String propName, JavaScriptObject value)/*-{ 
                                                                                                               obj[propName] = value;                 
                                                                                                               }-*/;

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

    public static final String getStacktraceAsHtml(final Throwable r) {
        try {
            if (r == null) {
                return "null";
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
                    stacktrace += ""
                            + getJavaScriptExceptionStackTrace(jsException.getException()).replaceAll("\n", "<br/>\n")
                            + "<br/>\n";
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
            return getStacktraceAsHtml(t);
        }

    }

    private static final Throwable unwrap(final Throwable e) {
        // Console.log("try unwrap " + e);
        // Console.log(ExporterUtil.wrap(e));
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
