package OnlineMarket.service;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.*;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.user.*;

import java.util.List;
import java.util.Map;

public interface UserService{

    User getByEmail(String email);

    User getByEvent(Event event);

    ResultObject<User> list(FilterForm filterForm);

    void delete(Integer id) throws UserNotFoundException, UserHasEventException, UserHasPostException, UserHasProductException, UserIsSuperAdminException;

    User getCurrentUser();

    void saveOriginal(User user);

    void createVerificationTokenForUser(final User user, final String token);

    VerificationToken generateNewVerificationToken(final String existingVerificationToken);

    VerificationToken getVerificationToken(String verificationToken);

    void createPasswordResetTokenForUser(User user, String token);

    PasswordResetToken generateNewPasswordResetToken(final String existingPasswordResetToken);

    PasswordResetToken getPasswordResetToken(String passwordResetToken);

    PasswordResetToken getPasswordResetTokenByUser(User user);

    void deleteVerificationToken(VerificationToken verificationToken);

    void deletePasswordResetToken(PasswordResetToken passwordResetToken);

    void update(User entity, boolean flagReset) throws CustomException;

    User getByDeclaration(String key, Object value);

    User getByKey(Integer key);

    void save(User entity);

    Map<String,Long> countUser();

    List<User> listAdmin();

    void changePass(User user, String password) throws CustomException;
}
