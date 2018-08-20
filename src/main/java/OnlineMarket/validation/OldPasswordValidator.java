package OnlineMarket.validation;



import OnlineMarket.form.user.ChangePassword;
import OnlineMarket.model.User;
import OnlineMarket.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class OldPasswordValidator implements ConstraintValidator<OldPasswordValid, ChangePassword> {

    private String message;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void initialize(OldPasswordValid arg0) {
        this.message = arg0.message();
    }

    @Override
    public boolean isValid(ChangePassword changePassword, ConstraintValidatorContext arg1) {

        boolean flag = false;
        User user = userService.getByKey(changePassword.getUserId());
        if(user != null && StringUtils.isNotBlank(changePassword.getOldPassword())){
           flag = passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword());
        }
        if(!flag){
            arg1.disableDefaultConstraintViolation();
            arg1
                    .buildConstraintViolationWithTemplate( this.message )
                    .addPropertyNode( "oldPassword" ).addConstraintViolation();
        }

        return flag;
    }

}
