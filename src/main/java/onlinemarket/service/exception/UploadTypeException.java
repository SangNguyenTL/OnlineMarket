package onlinemarket.service.exception;

public class UploadTypeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UploadTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UploadTypeException(String message) {
		super(message);
	}

}
