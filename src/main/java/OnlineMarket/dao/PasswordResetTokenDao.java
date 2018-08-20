package OnlineMarket.dao;

import OnlineMarket.model.PasswordResetToken;

import java.util.Date;

public interface PasswordResetTokenDao extends InterfaceDao<Long, PasswordResetToken> {
    void deleteAllExpiredSince(Date date);
}
