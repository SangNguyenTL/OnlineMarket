package onlinemarket.model.other;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import onlinemarket.model.Address;
import onlinemarket.model.User;
import onlinemarket.validation.PasswordsEqualConstraint;

@PasswordsEqualConstraint()
public class UserAddressNest {

	@Valid
	private User user;

	@Valid
	private Address address;

	@NotEmpty
	@Size(min = 6, max = 60)
	private String confirmPassword;

	@AssertTrue
	private boolean agree;

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public boolean isAgree() {
		return agree;
	}

	public void setAgree(boolean agree) {
		this.agree = agree;
	}

	public UserAddressNest() {
		this.user = new User();
		this.address = new Address();
	}

}
