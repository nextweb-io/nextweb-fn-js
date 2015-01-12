package io.nextweb.promise.js;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * <p>
 * A wrapper to provide access to <code>window.console</code>.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
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

    /**
     * <p>
     * Logs an object to the browser console if its available.
     * 
     * @param object
     *            The object to be logged to the console.
     */
    public static final void log(final JavaScriptObject object) {
        logNative(object);
    }

    private static final native void logNative(String message)/*-{
                                                              if (window.console) { 

                                                              window.console.log(message);
                                                              return;
                                                              }

                                                              }-*/;

    private static final native void logNative(JavaScriptObject message)/*-{
                                                                        if (window.console) { 

                                                                        window.console.log(message);
                                                                        return;
                                                                        }

                                                                        }-*/;

}
