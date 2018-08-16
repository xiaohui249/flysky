package com.sean.flysky.mysql.dbutil;

public class DBException extends RuntimeException {

	private static final long serialVersionUID = 8350049272861703406L;

	public DBException() {
		super();
	}

	public DBException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DBException(String message, Throwable cause) {
		super(message, cause);
	}

	public DBException(String message) {
		super(message);
	}

	public DBException(Throwable cause) {
		super(cause);
	}
	
	public DBException(Exception e) {
		super(e);
	}

}
