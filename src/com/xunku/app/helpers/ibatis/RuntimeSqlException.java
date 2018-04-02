package com.xunku.app.helpers.ibatis;

public class RuntimeSqlException extends RuntimeException {

	private static final long serialVersionUID = 7994706595118202004L;

	public RuntimeSqlException() {
		super();
	}

	public RuntimeSqlException(String message) {
		super(message);
	}

	public RuntimeSqlException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuntimeSqlException(Throwable cause) {
		super(cause);
	}

}
