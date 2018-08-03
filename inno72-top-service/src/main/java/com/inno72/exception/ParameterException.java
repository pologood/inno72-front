package com.inno72.exception;

/**
 * 参数自定义异常
 */
public class ParameterException extends RuntimeException {

	private static final long serialVersionUID = -1215693601086754541L;

	public ParameterException() {
	}

	public ParameterException(String message) {
		super(message);
	}

	public ParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParameterException(Throwable cause) {
		super(cause);
	}

	public ParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
