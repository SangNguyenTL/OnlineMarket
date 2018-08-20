package OnlineMarket.form.config;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;


public class LogoConfig {
	
	@NotEmpty
	@Size(max=2048)
	private String logo;
	
	@NotEmpty
	@Size(max=2048)
	private String favicon;

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getFavicon() {
		return favicon;
	}

	public void setFavicon(String favicon) {
		this.favicon = favicon;
	}
	
}
