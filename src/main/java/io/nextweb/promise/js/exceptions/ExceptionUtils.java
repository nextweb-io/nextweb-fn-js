package io.nextweb.promise.js.exceptions;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.UmbrellaException;

import io.nextweb.promise.exceptions.ExceptionResult;

public class ExceptionUtils {

    public static final Throwable convertToJavaException(final JavaScriptObject exception) {
        final Object obj = ExporterUtil.gwtInstance(exception);

        if (obj instanceof Throwable) {
            return (Throwable) obj;
        }

        if (obj instanceof String) {
            return new Exception((String) obj);
        }

        final String message = attemptToGetMessage(exception);

        if (message != null) {
            return new WrappedJSException(message, exception);
        }

        return new Exception(
                "Cannot convert reported exception result to Java Exception.\n" + "  Exception Result Type: "
                        + obj.getClass() + "\n" + "  Exception Result toString: " + obj.toString());
    }

    private static native final String attemptToGetMessage(
            JavaScriptObject exception)/*-{ 
                                       
                                       if (exception.message) {
                                       return exception.message;
                                       }
                                       
                                       return null;
                                       }-*/;

    // TODO does not seem to trigger clear exceptions!!!
    public static final void triggerExceptionCallback(final JavaScriptObject callback, final ExceptionResult r) {
        triggerFailureCallbackJs(callback, r.origin().getClass().toString(), unwrap(r.exception()).getMessage(),
                getStacktrace(r.exception()), getOriginTrace(), getJsException(r.exception()));
    }

    public static final JavaScriptObject createExceptionResult(final Object origin, final Throwable t) {
        return createExceptionResult(origin.toString(), t.getMessage(), getStacktrace(t), null, null);
    }

    // private static final native JavaScriptObject wrapExceptionResult(String
    // origin, String exceptionMessage,
    // String stacktrace, String originTrace, JavaScriptObject jsException)/*-{
    // return {
    // exception: exceptionMessage,
    // origin: origin,
    // origintrace: originTrace,
    // stacktrace: stacktrace,
    // jsException: jsException
    // }
    // }-*/;

    private static final native JavaScriptObject createExceptionResult(String origin, String exceptionMessage,
            String stacktrace, String originTrace, JavaScriptObject jsException)/*-{
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

    private static final native JavaScriptObject triggerFailureCallbackJs(JavaScriptObject callback, String origin,
            String exceptionMessage, String stacktrace, String originTrace, JavaScriptObject jsException)/*-{
                                                                                                          function Exception() {
                                                                                                          
                                                                                                          }
                                                                                                          
                                                                                                          Exception.prototype.toString = function() {return "Exception: "+exceptionMessage;  };
                                                                                                          var res = new Exception();
                                                                                                                                 
                                                                                                          res.message = message;                                      
                                                                                                          res.exception =exceptionMessage;
                                                                                                          res.origin = origin;
                                                                                                          res.origintrace = originTrace;
                                                                                                          res.stacktrace = stacktrace;
                                                                                                          res.stack = stacktrace;
                                                                                                          res.jsException = jsException;
                                                                                                                                                
                                                                                                          
                                                                                                         callback(res);
                                                                                                         }-*/;

    public static final JavaScriptObject wrapExceptionResult(final ExceptionResult r) {

        return ExceptionUtils.createExceptionResult(r.origin().getClass().toString(),
                unwrap(r.exception()).getMessage(), getStacktrace(r.exception()), getOriginTrace(),
                getJsException(r.exception()));
    }

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
