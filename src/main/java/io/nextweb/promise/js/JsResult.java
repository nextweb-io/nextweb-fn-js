package io.nextweb.promise.js;

import io.nextweb.promise.BasicPromise;
import io.nextweb.promise.js.exceptions.JsExceptionManager;
import io.nextweb.promise.js.wrapping.JsWrap;
import io.nextweb.promise.js.wrapping.WrapperCollection;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.ExporterUtil;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.fn.Closure;

/**
 * 
 * @author Max Rohde
 * 
 * @param <T>
 *            The return type of the Wrapped result
 * @param <R>
 *            The type of the wrapped result
 */
@Export
public class JsResult<T, R extends BasicPromise<T>> implements Exportable,
        JsBaseResult<JsResult<T, R>>, JsWrapper<R> {

    R result;
    WrapperCollection wrappers;

    @Override
    @Export
    public Object get(final Object... params) {

        if (params.length == 0) {
            return performGet();
        }

        if (params.length > 1) {
            throw new IllegalArgumentException(
                    "Only one argument of type JsClosure is supported.");
        }

        performGet(FnJs.asJsClosure((JavaScriptObject) params[0], wrappers));

        return ExporterUtil.wrap(this);
    }

    @Export
    @Override
    public JsResult<T, R> catchExceptions(final JsClosure exceptionListener) {
        exceptionManager().catchExceptions(exceptionListener);
        return this;
    }

    @NoExport
    private final JsExceptionManager exceptionManager() {
        return JsExceptionManager.wrap(result.getExceptionManager());
    }

    @Export
    @Override
    public JsResult<T, R> catchUndefined(final JsClosure undefinedListener) {
        exceptionManager().catchUndefined(undefinedListener);
        return this;
    }

    @Export
    @Override
    public JsResult<T, R> catchUnauthorized(final JsClosure unauthorizedListener) {
        exceptionManager().catchUnauthorized(unauthorizedListener);
        return this;
    }

    @Export
    @Override
    public JsResult<T, R> catchImpossible(final JsClosure impossibleListener) {
        exceptionManager().catchImpossible(impossibleListener);
        return this;
    }

    @NoExport
    private final Object performGet() {
        Object node = result.get();

        if (node != null) {
            node = JsWrap.unwrapBasicType(JsWrap
                    .forcewrapAnyObjectForJavaScript(node, wrappers));
        }

        return node;
    }

    @NoExport
    private final void performGet(final JsClosure onSuccess) {
        result.get(new Closure<T>() {

            @Override
            public void apply(final T o) {

                if (o instanceof JavaScriptObject) {
                    onSuccess.apply(o);
                    return;
                }

                onSuccess.apply(JsWrap.unwrapBasicType(JsWrap
                        .forcewrapAnyObjectForJavaScript(o, wrappers)));
            }

        });

    }

    @NoExport
    @Override
    public R getOriginal() {
        return result;
    }

    @NoExport
    @Override
    public void setOriginal(final R original) {
        this.result = original;
    }

    @NoExport
    public WrapperCollection getWrappers() {
        return wrappers;
    }

    @NoExport
    public void setWrappers(final WrapperCollection wrappers) {
        this.wrappers = wrappers;
    }

    public JsResult() {
        super();
    }

    @NoExport
    public static <T, R extends BasicPromise<T>> JsResult<T, R> wrap(
            final R result, final WrapperCollection wrappers) {
        final JsResult<T, R> jsResult = new JsResult<T, R>();
        jsResult.setOriginal(result);
        jsResult.setWrappers(wrappers);
        return jsResult;
    }

}
