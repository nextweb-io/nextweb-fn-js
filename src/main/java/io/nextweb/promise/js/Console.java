package io.nextweb.promise.js;

public final class Console {

    /**
     * <p>
     * Log a message to the browser console if available.
     * <p>
     * Otherwise, show an alert.
     * 
     * @param message
     *            The message to be displayed.
     */
    public static final void log(final String message) {
        logNative(message);
    }

    private static final native void logNative(String message)/*-{
                                                              if (window.console) { 

                                                              window.console.log(message);
                                                              return;
                                                              }

                                                              window.alert(message);

                                                              }-*/;

}
