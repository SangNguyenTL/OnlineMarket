package onlinemarket.form.config;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public class SocialConfig {
	
	@NotEmpty
	@Size(max=2048)
	@URL
	private String facebook;
	
	@NotEmpty
	@Size(max=2048)
	@URL
	private String googlePlus;
	
	@NotEmpty
	@Size(max=2048)
	@URL
	private String twitter;

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getGooglePlus() {
		return googlePlus;
	}

	public void setGooglePlus(String googlePlus) {
		this.googlePlus = googlePlus;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	
}
