package io.nextweb.promise.js.exceptions;

import io.nextweb.promise.Fn;
import io.nextweb.promise.exceptions.ExceptionListener;
import io.nextweb.promise.exceptions.ExceptionManager;
import io.nextweb.promise.exceptions.ExceptionResult;
import io.nextweb.promise.exceptions.ImpossibleListener;
import io.nextweb.promise.exceptions.ImpossibleResult;
import io.nextweb.promise.exceptions.UnauthorizedListener;
import io.nextweb.promise.exceptions.UnauthorizedResult;
import io.nextweb.promise.exceptions.UndefinedListener;
import io.nextweb.promise.exceptions.UndefinedResult;
import io.nextweb.promise.js.JsClosure;
import io.nextweb.promise.js.JsWrapper;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.core.client.JavaScriptObject;

@Export
public class JsExceptionManager implements Exportable,
        JsWrapper<ExceptionManager>, JsExceptionListeners<JsExceptionManager> {

    private ExceptionManager em;

    @Export
    public void onFailure(final JavaScriptObject origin,
            final String errorMessage) {
        em.onFailure(Fn.exception(origin, new Exception(errorMessage)));
    }

    @Override
    @Export
    public JsExceptionManager catchExceptions(final JsClosure exceptionListener) {
        em.catchExceptions(new ExceptionListener() {

            @Override
            public void onFailure(final ExceptionResult r) {

                exceptionListener.apply(ExceptionUtils.wrapExceptionResult(r));

            }
        });

        return this;
    }

    @Export
    @Override
    public JsExceptionManager catchUndefined(final JsClosure undefinedListener) {
        em.catchUndefined(new UndefinedListener() {

            @Override
            public void onUndefined(final UndefinedResult r) {
                undefinedListener.apply(wrapUndefinedResult(r.origin()
                        .getClass().toString(), r.message()));
            }
        });
        return this;
    }

    @Export
    @Override
    public JsExceptionManager catchUnauthorized(
            final JsClosure unauthorizedListener) {
        em.catchUnauthorized(new UnauthorizedListener() {

            @Override
            public void onUnauthorized(final UnauthorizedResult r) {
                unauthorizedListener.apply(wrapUnauthorizedResult(r.origin()
                        .getClass().toString(), r.getMessage(), r.getType()
                        .getClass().toString()));
            }
        });
        return this;
    }

    @Export
    @Override
    public JsExceptionManager catchImpossible(final JsClosure impossibleListener) {
        em.catchImpossible(new ImpossibleListener() {

            @Override
            public void onImpossible(final ImpossibleResult ir) {
                impossibleListener.apply(wrapImpossibleResult(ir.origin()
                        .getClass().toString(), ir.message(), ir.cause()
                        .getClass().toString()));
            }
        });
        return this;
    }

    public JsExceptionManager() {
        super();
    }

    @NoExport
    @Override
    public ExceptionManager getOriginal() {
        return this.em;
    }

    @NoExport
    @Override
    public void setOriginal(final ExceptionManager original) {
        this.em = original;
    }

    @NoExport
    public static final native JavaScriptObject wrapUndefinedResult(
            String origin, String message)/*-{
                                          return {
                                          message: message,
                                          origin: origin
                                          }
                                          }-*/;

    @NoExport
    public static final native JavaScriptObject wrapUnauthorizedResult(
            String origin, String message, String type)/*-{
                                                       return {
                                                       message: message,
                                                       origin: origin,
                                                       type: type
                                                       }
                                                       }-*/;

    @NoExport
    public static final native JavaScriptObject wrapImpossibleResult(
            String origin, String message, String cause)/*-{
                                                        return {
                                                        message: message,
                                                        origin: origin,
                                                        cause: cause
                                                        }
                                                        }-*/;

    @NoExport
    public static JsExceptionManager wrap(final ExceptionManager em) {
        final JsExceptionManager jsem = new JsExceptionManager();
        jsem.setOriginal(em);
        return jsem;
    }

}
