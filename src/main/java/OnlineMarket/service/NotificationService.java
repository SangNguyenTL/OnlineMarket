package OnlineMarket.service;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Notification;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.user.UserNotFoundException;

public interface NotificationService {
    ResultObject<Notification> listByUser(Integer id, FilterForm filterForm) throws CustomException;

    void add(Notification notification) throws UserNotFoundException;

    void modifyStatus(int id, byte status) throws CustomException;

    long countByUser() throws CustomException;

    void delete(int id) throws CustomException;
}
