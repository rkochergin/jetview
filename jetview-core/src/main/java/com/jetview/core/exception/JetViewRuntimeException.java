package com.jetview.core.exception;

/**
 * @author Roman Kochergin
 */
public class JetViewRuntimeException extends RuntimeException {

    public JetViewRuntimeException() {
        super();
    }

    public JetViewRuntimeException(String message) {
        super(message);
    }

    public JetViewRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JetViewRuntimeException(Throwable cause) {
        super(cause);
    }
}
