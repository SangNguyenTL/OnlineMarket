package onlinemarket.form.config;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class ApiConfig {

	@NotEmpty
	@Size(max = 100)
	private String apiFacebook;

	@NotEmpty
	@Size(max = 100)
	private String apiGoogle = "AIzaSyDwgEWMLktEQnK7Cn8SBMDTV6jn1I1G3fc";

	public String getApiFacebook() {
		return apiFacebook;
	}

	public void setApiFacebook(String apiFacebook) {
		this.apiFacebook = apiFacebook;
	}

	public String getApiGoogle() {
		return apiGoogle;
	}

	public void setApiGoogle(String apiGoogle) {
		this.apiGoogle = apiGoogle;
	}

}
