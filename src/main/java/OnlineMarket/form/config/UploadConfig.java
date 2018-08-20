package OnlineMarket.form.config;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

public class UploadConfig {
	
	@NotNull
	@Range(min = 1, max = 10)
	private Integer maxFileSize = 5;
	
	@NotNull
	@Range(min = 1, max = 25)
	private Integer maxSize = 25;
 
	public Integer getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(Integer maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public Integer getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}
	
	
}
