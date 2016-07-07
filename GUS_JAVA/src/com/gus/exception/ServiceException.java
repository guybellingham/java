package com.gus.exception;

/**
 * A generic (checked) 'service' Exception that may contain a <code>message</code>  
 * a <code>cause</code> and a <code>httpStatusCode</code>.  
 * @author Guy
 */
public class ServiceException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private int httpStatusCode;
	/**
     * Creates an exception to return the given HTTP status code to the client.
     * @param message the message, which <em>may</em> be displayed to end users.
     */
    public ServiceException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception to return a 500 INTERNAL SERVER ERROR to the client.
     * @param cause the underlying cause.
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates an exception to return a 500 INTERNAL SERVER ERROR to the client.
     * @param message the message, which <em>may</em> be displayed to end users.
     * @param cause the underlying cause.
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates an exception to return the given HTTP status code to the client.
     * @param message the message, which <em>may</em> be displayed to end users.
     * @param httpStatusCode the status code.
     */
    public ServiceException(String message, int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * Creates an exception to return the given HTTP status code to the client.
     * @param message the message, which <em>may</em> be displayed to end users.
     * @param cause the underlying cause.
     * @param httpStatusCode the status code.
     */
    public ServiceException(String message, Throwable cause, int httpStatusCode) {
        super(message, cause);
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * Creates an exception to return the given HTTP status code to the client.
     * @param cause the underlying cause.
     * @param httpStatusCode the status code.
     */
    public ServiceException(Throwable cause, int httpStatusCode) {
        super(cause);
        this.httpStatusCode = httpStatusCode;
    }



    /**
     * Returns the HTTP status code that should be returned to the requesting client.
     * @return the code, such as {@link HttpServletResponse#SC_INTERNAL_SERVER_ERROR} or
     *         {@link HttpServletResponse#SC_BAD_REQUEST}.
     */
    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
