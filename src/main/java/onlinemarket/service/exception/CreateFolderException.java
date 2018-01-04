package onlinemarket.service.exception;

public class CreateFolderException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CreateFolderException(String message, Throwable cause) {
		super(message, cause);
	}

	public CreateFolderException(String message) {
		super(message);
	}

}
