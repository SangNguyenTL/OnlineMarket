package onlinemarket.form.config;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class ContactConfig {
	
	@NotEmpty
	@Email
	@Size( min = 6, max = 125 )
	private String email;
	
	@NotEmpty
	@Pattern(regexp = "^(\\-?\\d+(\\.\\d+)?),\\s*(\\-?\\d+(\\.\\d+)?)$")
	@Size(max = 125)
	private String latitude = "10.8014659";
	
	@NotEmpty
	@Pattern(regexp = "^(\\-?\\d+(\\.\\d+)?),\\s*(\\-?\\d+(\\.\\d+)?)$")
	@Size(max = 125)
	private String longitude= "106.6525974";
	
	@NotEmpty
	@Pattern(regexp="(^$|[0-9]{10,11})")
	private String phoneNumber;
	
	@NotEmpty
	@Size(min = 10, max = 255)
	private String address;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
