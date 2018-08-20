package OnlineMarket.dao;

import OnlineMarket.model.User;
import OnlineMarket.model.VerificationToken;

import java.util.Date;
import java.util.List;

public interface VerificationTokenDao extends InterfaceDao<Long, VerificationToken> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

    List<VerificationToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    void deleteAllExpiredSince(Date now);

}
