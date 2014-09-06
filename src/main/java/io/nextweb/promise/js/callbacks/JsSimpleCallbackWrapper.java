package io.nextweb.promise.js.callbacks;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExporterUtil;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.callbacks.SimpleCallback;

@Export
public class JsSimpleCallbackWrapper {

    @NoExport
    SimpleCallback wrapped;

    public JsSimpleCallbackWrapper() {
        super();
    }

    @Export
    public void onFailure(final JavaScriptObject t) {
        final Object gwtInstance = ExporterUtil.gwtInstance(t);

        if (gwtInstance instanceof Throwable) {
            wrapped.onFailure((Throwable) gwtInstance);
            return;
        }

        wrapped.onFailure(new Exception("Failure while processing native JavaScript function."));
    }

    @Export
    public void onSuccess() {
        wrapped.onSuccess();
    }

}
