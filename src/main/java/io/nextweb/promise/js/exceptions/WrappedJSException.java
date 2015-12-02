package io.nextweb.promise.js.exceptions;

import com.google.gwt.core.client.JavaScriptObject;

public class WrappedJSException extends Exception {

    private static final long serialVersionUID = 1L;

    private final JavaScriptObject original;

    public JavaScriptObject getOriginal() {
        return original;
    }

    public WrappedJSException(final String message, final JavaScriptObject original) {
        super(message);
        this.original = original;
    }

}
