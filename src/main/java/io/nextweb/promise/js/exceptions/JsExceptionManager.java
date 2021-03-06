package io.nextweb.promise.js.exceptions;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.core.client.JavaScriptObject;

import io.nextweb.promise.Fn;
import io.nextweb.promise.exceptions.DataExceptionManager;
import io.nextweb.promise.exceptions.ExceptionListener;
import io.nextweb.promise.exceptions.ExceptionResult;
import io.nextweb.promise.exceptions.ImpossibleListener;
import io.nextweb.promise.exceptions.ImpossibleResult;
import io.nextweb.promise.exceptions.UnauthorizedListener;
import io.nextweb.promise.exceptions.UnauthorizedResult;
import io.nextweb.promise.exceptions.UndefinedListener;
import io.nextweb.promise.exceptions.UndefinedResult;
import io.nextweb.promise.js.JsClosure;
import io.nextweb.promise.js.JsWrapper;

/**
 * <p>
 * A manager for listeners to exceptions.
 * <p>
 * Allows defining listeners and to trigger exceptions that will call these
 * listeners.
 * 
 * <p>
 * To learn more about exception management see <a href=
 * 'https://beta.objecthub.io/dev/~001/users/~root/home/xplr/.n/ObjectHub_Documentation/.n/API/.n/API_Building_Blocks/.n/Exception_Handling.html'>Exception
 * Handling</a>.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
@Export
public class JsExceptionManager
        implements Exportable, JsWrapper<DataExceptionManager>, JsExceptionListeners<JsExceptionManager> {

    @NoExport
    private DataExceptionManager em;

    /**
     * <p>
     * Calling this method will trigger an exception.
     * 
     * @param origin
     *            A context object for the exception.
     * @param errorMessage
     *            An error message for this exception.
     */
    @Export
    public void onFailure(final JavaScriptObject origin, final String errorMessage) {
        em.onFailure(Fn.exception(origin, new Exception(errorMessage)));
    }

    /**
     * <P>
     * Allows defining a listener for all possible error situations.
     * <p>
     * The parameter will be called with one argument, which will be an object
     * with the following properties:
     * 
     * <pre>
     * em.catchExceptions(function(result) {
     *    console.log('Message: '+result.message);
     *    console.log('Stacktrace: '+result.stack);
     *    console.log('Raw JavaScript exception: '+result.jsException); 
     * });
     * 
     * 
     * </pre>
     * 
     * @param exceptionListner
     *            A JavaScrip callback that will be triggered if an exception
     *            occurred.
     * 
     * @return This exception manager object.
     */
    @Override
    @Export
    public JsExceptionManager catchExceptions(final JsClosure exceptionListener) {
        // TODO conflicts with Appjangle IDE

        // if (exceptionListener == null) {
        // throw new NullPointerException("Specified listenered for
        // catchExceptions must not be null.");
        // }
        // Console.log("Register listener " + exceptionListener);

        em.catchExceptions(new ExceptionListener() {

            @Override
            public void onFailure(final ExceptionResult r) {

                try {
                    exceptionListener.apply(ExceptionUtils.convertToJSExceptionResult(r));
                } catch (final Throwable t) {

                    final DataExceptionManager parentExceptionManager = em.getParentExceptionManager();

                    // Console.log(JsExceptionManager.this + ": Caught exception
                    // in block processing exception: " + t);
                    // Console.log(ExceptionUtils.getStacktraceAsHtml(t));

                    if (parentExceptionManager != null) {
                        parentExceptionManager.onFailure(Fn.exception(this, t));
                        return;
                    }

                    throw new RuntimeException(t);

                }

            }
        });

        return this;
    }

    /**
     * <p>
     * Allows defining a listener when an error is triggered by data not being
     * defined.
     * <p>
     * The listener will be called with one argument which will be a JavaScript
     * object with the property 'message'.
     * <p>
     * For instance:
     * 
     * <pre>
     * em.catchUndefined(function(undefinedResult) {
     *    console.log('Message: '+undefinedResult.message);
     * });
     * </pre>
     * 
     * @param undefinedListener
     *            A JavaScript callback that will be called when data is not
     *            defined.
     * 
     * @return This exception manager object.
     */
    @Export
    @Override
    public JsExceptionManager catchUndefined(final JsClosure undefinedListener) {
        em.catchUndefined(new UndefinedListener() {

            @Override
            public void onUndefined(final UndefinedResult r) {
                undefinedListener.apply(wrapUndefinedResult(r.origin().getClass().toString(), r.message()));
            }
        });
        return this;
    }

    /**
     * <p>
     * Allows defining a listener that will be called when any error related to
     * insufficient authorizations occurs.
     * <p>
     * The listener will be called with one argument which will be a JavaScript
     * object with the property 'message'.
     * <p>
     * For instance:
     * 
     * <pre>
     * em.catchUnauthorized(function(unauthorizedResult) {
     *    console.log('Message: '+unauthorizedResult.message);
     * });
     * </pre>
     * 
     * @param unauthorizedListener
     *            A JavaScript callback that will be called when operations are
     *            not authorized.
     * 
     * @return This exception manager object.
     */
    @Export
    @Override
    public JsExceptionManager catchUnauthorized(final JsClosure unauthorizedListener) {
        em.catchUnauthorized(new UnauthorizedListener() {

            @Override
            public void onUnauthorized(final UnauthorizedResult r) {
                unauthorizedListener.apply(wrapUnauthorizedResult(r.origin().getClass().toString(), r.getMessage(),
                        r.getType().getClass().toString()));
            }
        });
        return this;
    }

    /**
     * <p>
     * Allows defining a listener that will be called when any data operation is
     * impossible.
     * <p>
     * The listener will be called with one argument which will be a JavaScript
     * object with the property 'message'.
     * <p>
     * For instance:
     * 
     * <pre>
     * em.catchImpossible(function(impossibleResult) {
     *    console.log('Message: '+impossibleResult.message);
     * });
     * </pre>
     */
    @Export
    @Override
    public JsExceptionManager catchImpossible(final JsClosure impossibleListener) {
        em.catchImpossible(new ImpossibleListener() {

            @Override
            public void onImpossible(final ImpossibleResult ir) {
                impossibleListener.apply(wrapImpossibleResult(ir.origin().getClass().toString(), ir.message(),
                        ir.cause().getClass().toString()));
            }
        });
        return this;
    }

    public JsExceptionManager() {
        super();
    }

    @NoExport
    @Override
    public DataExceptionManager getOriginal() {
        return this.em;
    }

    @NoExport
    @Override
    public void setOriginal(final DataExceptionManager original) {
        this.em = original;
    }

    @NoExport
    public static final native JavaScriptObject wrapUndefinedResult(String origin, String message)/*-{
                                                                                                  return {
                                                                                                  message: message,
                                                                                                  origin: origin
                                                                                                  }
                                                                                                  }-*/;

    @NoExport
    public static final native JavaScriptObject wrapUnauthorizedResult(String origin, String message, String type)/*-{
                                                                                                                  return {
                                                                                                                  message: message,
                                                                                                                  origin: origin,
                                                                                                                  type: type
                                                                                                                  }
                                                                                                                  }-*/;

    @NoExport
    public static final native JavaScriptObject wrapImpossibleResult(String origin, String message, String cause)/*-{
                                                                                                                 return {
                                                                                                                 message: message,
                                                                                                                 origin: origin,
                                                                                                                 cause: cause
                                                                                                                 }
                                                                                                                 }-*/;

    @NoExport
    public static JsExceptionManager wrap(final DataExceptionManager em) {
        final JsExceptionManager jsem = new JsExceptionManager();
        jsem.setOriginal(em);
        return jsem;
    }

}
