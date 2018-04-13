package onlinemarket.form.config;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class GeneralConfig {
	
	@NotEmpty
	@Size(min = 3, max = 50)
	private String title;
	
	@NotEmpty
	@Size(min = 3, max = 150)
	private String keyword;
	
	@NotEmpty
	@Size(min = 30, max = 250)
	private String description;

	@NotEmpty
	@Size(max = 350)
	private String footerText;

	@NotEmpty
	@Size(max = 150)
	private String licenseText;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFooterText() {
		return footerText;
	}

	public void setFooterText(String footerText) {
		this.footerText = footerText;
	}

	public String getLicenseText() {
		return licenseText;
	}

	public void setLicenseText(String licenseText) {
		this.licenseText = licenseText;
	}
}
