package io.nextweb.promise.js;

import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.functional.Function;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.ExporterUtil;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.core.client.JavaScriptObject;

import io.nextweb.promise.BasicPromise;
import io.nextweb.promise.exceptions.DataExceptionManager;
import io.nextweb.promise.exceptions.ExceptionListener;
import io.nextweb.promise.exceptions.ExceptionResult;
import io.nextweb.promise.js.callbacks.PromiseToAsyncJsOperationWrapper;
import io.nextweb.promise.js.exceptions.ExceptionUtils;
import io.nextweb.promise.js.exceptions.JsExceptionManager;

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
public class JsDataPromise<T, R extends BasicPromise<T>>
        implements Exportable, JsBasicPromise<JsDataPromise<T, R>>, JsWrapper<R> {

    protected R result;
    // protected WrapperCollection wrappers;

    protected Function<Object, Object> wrapper;

    @Override
    @Export
    public Object get(final Object... params) {

        if (params.length == 0) {
            return performGet();
        }

        if (params.length > 1) {
            throw new IllegalArgumentException(
                    "Only either no argument or one argument of type JsClosure is supported.");
        }

        performGet(FnJs.asJsClosure((JavaScriptObject) params[0], new ExceptionListener() {

            @Override
            public void onFailure(final ExceptionResult r) {
                exceptionManager().getOriginal().onFailure(r);
            }
        }));

        return ExporterUtil.wrap(this);
    }

    @Export
    public void apply(final JavaScriptObject callback) {
        if (!FnJs.isJsFunction(callback)) {
            throw new IllegalArgumentException("Expected one parameter which is a function.");
        }

        result.apply(new ValueCallback<T>() {

            @Override
            public void onFailure(final Throwable t) {
                FnJs.asClosure2(callback).apply(ExceptionUtils.createExceptionResult(this, t), null);

            }

            @Override
            public void onSuccess(final T value) {
                FnJs.asClosure2(callback).apply(null, wrapper.apply(value));
            }
        });

    }

    @Export
    @Override
    public JsDataPromise<T, R> catchExceptions(final JsClosure exceptionListener) {
        exceptionManager().catchExceptions(exceptionListener);
        return this;
    }

    @NoExport
    private final JsExceptionManager exceptionManager() {
        return JsExceptionManager.wrap(result.getExceptionManager());
    }

    @NoExport
    public final DataExceptionManager javaExceptionManager() {
        return result.getExceptionManager();
    }

    @Export
    @Override
    public JsDataPromise<T, R> catchUndefined(final JsClosure undefinedListener) {
        exceptionManager().catchUndefined(undefinedListener);
        return this;
    }

    @Export
    @Override
    public JsDataPromise<T, R> catchUnauthorized(final JsClosure unauthorizedListener) {
        exceptionManager().catchUnauthorized(unauthorizedListener);
        return this;
    }

    @Export
    @Override
    public JsDataPromise<T, R> catchImpossible(final JsClosure impossibleListener) {
        exceptionManager().catchImpossible(impossibleListener);
        return this;
    }

    @Export
    public Object func() {
        return PromiseToAsyncJsOperationWrapper.wrapSafe(wrapper, result);
    }

    @NoExport
    protected final Object performGet() {
        Object node = result.get();

        if (node != null) {
            node = wrapper.apply(node);

        }

        return node;
    }

    @NoExport
    protected final void performGet(final JsClosure onSuccess) {
        result.get(new Closure<T>() {

            @Override
            public void apply(final T o) {

                if (o instanceof JavaScriptObject) {
                    onSuccess.apply(o);
                    return;
                }

                onSuccess.apply(wrapper.apply(o));
                // JsWrap.unwrapBasicType(JsWrap.forcewrapAnyObjectForJavaScript(o,
                // wrappers)));
            }

        });

    }

    @NoExport
    public final void apply(final ValueCallback<Object> callback) {
        result.apply(new ValueCallback<T>() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess(final T value) {
                if (value instanceof JavaScriptObject) {
                    callback.onSuccess(value);
                    return;
                }

                callback.onSuccess(wrapper.apply(value));
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
    public void setWrapper(final Function<Object, Object> wrapper) {
        this.wrapper = wrapper;
    }

    public JsDataPromise() {
        super();
    }

    @NoExport
    public static <T, R extends BasicPromise<T>> JsDataPromise<T, R> wrap(final R result,
            final Function<Object, Object> wrapper) {
        final JsDataPromise<T, R> jsResult = new JsDataPromise<T, R>();
        jsResult.setOriginal(result);
        jsResult.setWrapper(wrapper);
        return jsResult;
    }

}
