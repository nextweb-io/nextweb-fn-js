package io.nextweb.promise.js;

import com.google.gwt.core.client.JavaScriptObject;

public final class Console {

    /**
     * <p>
     * Log a message to the browser console if available.
     * <p>
     * Otherwise, do nothing.
     * 
     * @param message
     *            The message to be displayed.
     */
    public static final void log(final String message) {
        logNative(message);
    }

    public static final void log(final JavaScriptObject object) {
        logNativeObject(message);
    }

    private static final native void logNative(String message)/*-{
                                                              if (window.console) { 

                                                              window.console.log(message);
                                                              return;
                                                              }

                                                              }-*/;

}
