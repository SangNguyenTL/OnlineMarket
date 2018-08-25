package OnlineMarket.result.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult {
	private boolean error;
	
	private String message;

	private ValidationErrorDTO validationErrorDTO;

    private List<?> list;

    public ResponseResult() {
        list = new ArrayList<>();
    }

    public ResponseResult(boolean error) {
        super();
        this.error = error;
    }

    public ResponseResult(boolean error, String message) {
		super();
		this.error = error;
		this.message = message;
	}

	public ResponseResult(boolean error, ValidationErrorDTO validationErrorDTO) {
        super();
		this.error = error;
		this.validationErrorDTO = validationErrorDTO;
	}

	public ResponseResult(boolean error, List<?>  list) {
        super();
		this.error = error;
		this.list = list;
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

	public ValidationErrorDTO getValidationErrorDTO() {
		return validationErrorDTO;
	}

	public void setValidationErrorDTO(ValidationErrorDTO validationErrorDTO) {
		this.validationErrorDTO = validationErrorDTO;
	}

    public List<?>  getList() {
        return list;
    }

    public void setList(List<?>  list) {
        this.list = list;
    }
}
