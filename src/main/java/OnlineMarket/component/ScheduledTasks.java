package OnlineMarket.component;

import OnlineMarket.dao.PasswordResetTokenDao;
import OnlineMarket.dao.ProductViewsStatisticDao;
import OnlineMarket.dao.VerificationTokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTasks {

    @Autowired
    ProductViewsStatisticDao productViewsStatisticDao;

    @Autowired
    VerificationTokenDao verificationTokenDao;

    @Autowired
    PasswordResetTokenDao passwordResetTokenDao;

    @Scheduled(cron = "0 0 0 * * *")
    void resetDay(){
        verificationTokenDao.deleteAllExpiredSince(new Date());
        passwordResetTokenDao.deleteAllExpiredSince(new Date());
    }

    @Scheduled(cron = "0 0 0 * * MON")
    void resetWeek(){
        productViewsStatisticDao.resetWeek();
    }

    @Scheduled(cron = "0 0 0 1 * *")
    void resetMonth(){
        productViewsStatisticDao.resetMonth();
    }

    @Scheduled(cron = "0 0 0 1 1 *")
    void resetYear(){
        productViewsStatisticDao.resetYear();
    }
}
