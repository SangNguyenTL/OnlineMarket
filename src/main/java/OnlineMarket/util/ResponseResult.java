package OnlineMarket.util;

public class ResponseResult {
	private boolean error;
	
	private String message;

	public ResponseResult(boolean error, String message) {
		super();
		this.error = error;
		this.message = message;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
