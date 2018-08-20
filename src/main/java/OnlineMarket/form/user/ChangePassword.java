package OnlineMarket.form.user;

import OnlineMarket.validation.OldPasswordValid;
import OnlineMarket.validation.PasswordsEqualConstraint;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordsEqualConstraint
@OldPasswordValid
public class ChangePassword {

    private Integer userId;

    private String password;

    private String confirmPassword;

    private String oldPassword;

    @Size(min = 6, max = 60)
    @NotEmpty
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Size(min = 6, max = 60)
    @NotEmpty
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Size(min = 6, max = 60)
    @NotEmpty
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @NotNull
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
