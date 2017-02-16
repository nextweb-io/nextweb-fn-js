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
import io.nextweb.promise.Fn;
import io.nextweb.promise.exceptions.DataExceptionManager;
import io.nextweb.promise.exceptions.ExceptionListener;
import io.nextweb.promise.exceptions.ExceptionResult;
import io.nextweb.promise.js.callbacks.PromiseToAsyncJsOperationWrapper;
import io.nextweb.promise.js.exceptions.ExceptionUtils;
import io.nextweb.promise.js.exceptions.JsExceptionManager;

/**
 * <p>
 * PLEASE USE NAME 'PROMISE' for this object.
 * <p>
 * A promise object that is linked to an asynchronous operation.
 * <p>
 * Calling the {@link #get(Object...)} or {{@link #apply(JavaScriptObject)}
 * methods of this object will trigger the asynchronous operation and return the
 * result of the operation.
 * <p>
 * The operation for the promise is guaranteed to be only performed once. If
 * {@link #get(Object...)} and {{@link #apply(JavaScriptObject)} are called
 * again, a cached result is returned.
 * 
 * 
 * @author Max Rohde
 * 
 */
@Export
public class JsDataPromise<T, R extends BasicPromise<T>>
        implements Exportable, JsBasicPromise<JsDataPromise<T, R>>, JsWrapper<R> {

    protected R result;
    // protected WrapperCollection wrappers;

    protected Function<Object, Object> wrapper;

    /**
     * <p>
     * This method will attempt to get the result of this promise.
     * <p>
     * If the promise has already been resolved, the last result obtained is
     * returned (e.g. the promise is not resolved anew).
     * <p>
     * A JavaScript function can be supplied as optional parameter. This
     * JavaScript function will be called with the result as the only argument
     * when the operation was successful.
     * 
     * <pre>
     * promise.get(function(result) {
     *    ...
     * });
     * </pre>
     * <p>
     * If the operation was not successful, one of the defined exception
     * interceptors will be called.
     * <p>
     * Note: Calling get() without any argument will ignore all defined
     * exception interceptors to assure termination of the statement.
     * 
     * @return The result that can be obtained by resolving this promise (or
     *         null if a the promise is not yet resolved and/or cannot be
     *         resolved synchronously).
     */
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

            }

        });

    }

    /**
     * <p>
     * This method will attempt to get the result for this promise.
     * <p>
     * This method must be supplied with a callback function. This function will
     * be called when the promise has been resolved.
     * <p>
     * The callback function should be in the format:
     * 
     * <pre>
     * apply(function(ex, res) {
     * 
     * });
     * </pre>
     * 
     * Where <code>ex</code> will contain an exception object when the operation
     * failed, or be <code>null</code> otherwise. <code>res</code> will be the
     * result of the operation or <cocde>null</code> when the operation fails.
     * 
     * @param callback
     *            A callback function with two arguments, <code>ex</code> and
     *            <code>res</code>
     */
    @Export
    public void apply(final JavaScriptObject callback) {
        if (!FnJs.isJsFunction(callback)) {
            throw new IllegalArgumentException("Expected one parameter which is a function but received: " + callback);
        }

        result.apply(new ValueCallback<T>() {

            @Override
            public void onFailure(final Throwable t) {
                FnJs.asClosure2(callback).apply(ExceptionUtils.convertToJSExceptionResult(Fn.exception(this, t)), null);

            }

            @Override
            public void onSuccess(final T value) {
                FnJs.asClosure2(callback).apply(null, wrapper.apply(value));
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

    /**
     * <p>
     * Allows defining a listener function which is called when any error occurs
     * during executing the operation for this promise.
     * <p>
     * For instance
     * 
     * <pre>
     * catchExceptions(function(ex) { ... } );
     * </pre>
     * <p>
     * To learn more about managing errors and exceptions, see <a href=
     * 'https://beta.objecthub.io/dev/~001/users/~root/home/xplr/.n/ObjectHub_Documentation/.n/API/.n/API_Building_Blocks/.n/Exception_Handling'>
     * Exception Handling</a>.
     * 
     * @param exceptionListener
     *            A function with one argument <code>ex</code>.
     */
    @Export
    @Override
    public JsDataPromise<T, R> catchExceptions(final JsClosure exceptionListener) {
        exceptionManager().catchExceptions(exceptionListener);
        return this;
    }

    /**
     * <p>
     * Like {@link #catchExceptions(JsClosure)} - but this listener is only
     * called when an error occurs which is caused by an item of data missing.
     */
    @Export
    @Override
    public JsDataPromise<T, R> catchUndefined(final JsClosure undefinedListener) {
        exceptionManager().catchUndefined(undefinedListener);
        return this;
    }

    /**
     * <p>
     * Like {@link #catchExceptions(JsClosure)} - but this listener is only
     * called when an error occurs which is caused by an insufficient
     * authorizations for accessing an item of data.
     */
    @Export
    @Override
    public JsDataPromise<T, R> catchUnauthorized(final JsClosure unauthorizedListener) {
        exceptionManager().catchUnauthorized(unauthorizedListener);
        return this;
    }

    /**
     * <p>
     * Like {@link #catchExceptions(JsClosure)} - but this listener is only
     * called when an error occurs which is caused by an operation on data not
     * being possible.
     */
    @Export
    @Override
    public JsDataPromise<T, R> catchImpossible(final JsClosure impossibleListener) {
        exceptionManager().catchImpossible(impossibleListener);
        return this;
    }

    /**
     * 
     * @return A function that wraps this promise as an asynchronous operation.
     */
    @Deprecated
    @Export
    public Object func() {
        return PromiseToAsyncJsOperationWrapper.wrapSafe(wrapper, result);
    }

    @NoExport
    private final JsExceptionManager exceptionManager() {
        return JsExceptionManager.wrap(result.getExceptionManager());
    }

    @NoExport
    public final DataExceptionManager javaExceptionManager() {
        return result.getExceptionManager();
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
