package io.nextweb.fn.js.exceptions;

import io.nextweb.fn.exceptions.ExceptionResult;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.UmbrellaException;

public class ExceptionUtils {

    public static final void triggerExceptionCallback(
            final JavaScriptObject callback, final ExceptionResult r) {
        triggerFailureCallbackJs(callback, r.origin().getClass().toString(),
                unwrap(r.exception()).getMessage(),
                getStacktrace(r.exception()));
    }

    public static final JavaScriptObject wrapExceptionResult(
            final ExceptionResult r) {

        return (ExceptionUtils.wrapExceptionResult(r.origin().getClass()
                .toString(), unwrap(r.exception()).getMessage(),
                getStacktrace(r.exception())));
    }

    private static final String getStacktrace(final Throwable r) {
        String stacktrace = unwrap(r).toString() + "<br />";

        for (final StackTraceElement element : unwrap(r).getStackTrace()) {
            stacktrace += element + "<br/>";
        }

        stacktrace += getCauseTrace(r);
        return stacktrace;
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

        String res = "Caused By: " + unwrap(t.getCause()).toString() + "<br/>";
        for (final StackTraceElement element : unwrap(t.getCause())
                .getStackTrace()) {
            res += element + "<br/>";
        }

        return res;
    }

    private static final native JavaScriptObject triggerFailureCallbackJs(
            JavaScriptObject callback, String origin, String exceptionMessage,
            String stacktrace)/*-{
                              callback({
                              exception: exceptionMessage,
                              origin: origin,
                              stacktrace: stacktrace
                              });
                              }-*/;

    private static final native JavaScriptObject wrapExceptionResult(
            String origin, String exceptionMessage, String stacktrace)/*-{
                                                                      return {
                                                                      exception: exceptionMessage,
                                                                      origin: origin,
                                                                      stacktrace: stacktrace
                                                                      }
                                                                      }-*/;

}
