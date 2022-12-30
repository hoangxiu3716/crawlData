package de.gimik.apps.parsehub.backend;

public class BackendException extends RuntimeException {

	private static final long serialVersionUID = 2024934252853606808L;
	private String errorCode;

	public BackendException(String errorCode){
		super();
		
		this.errorCode = errorCode;
	}
	
	public BackendException(String message, String errorCode){
		super(message);
		
		this.errorCode = errorCode;
	}
	
	public BackendException(Exception innerException, String errorCode){
		super(innerException);
		
		this.errorCode = errorCode;
	}
	
	public BackendException(String message, Exception innerException, String errorCode){
		super(message, innerException);
		
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}	
	
}
