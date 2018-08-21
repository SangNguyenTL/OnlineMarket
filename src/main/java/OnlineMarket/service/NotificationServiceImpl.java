package OnlineMarket.service;

import OnlineMarket.dao.NotificationDao;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Notification;
import OnlineMarket.model.User;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.user.UserNotFoundException;
import OnlineMarket.util.other.NotificationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service("notificationService")
@Transactional
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    NotificationDao notificationDao;

    @Autowired
    UserService userService;

    @Override
    public ResultObject<Notification> listByUser(Integer id, FilterForm filterForm) throws CustomException {

        User user = userService.getByKey(id);
        if(user == null) throw new CustomException("User not found");
        return notificationDao.listByDeclaration("user", user, filterForm);
    }

    @Override
    public void add(Notification notification) throws UserNotFoundException {
        if(notification.getUser() == null || userService.getByKey(notification.getUser().getId()) == null) throw new UserNotFoundException();
        notification.setCreateDate(new Date());
        notification.setStatus(NotificationStatus.UNREAD.getId());
        notificationDao.merge(notification);
    }

    @Override
    public void modifyStatus(int id, byte status) throws CustomException {
        Notification notification = notificationDao.getByKey(id);
        if(notification == null) throw new CustomException("Notification not found");
        if(status != NotificationStatus.UNREAD.getId() && status != NotificationStatus.SEEN.getId()) throw new CustomException("Status is invalid");
        notification.setStatus(status);
        notificationDao.merge(notification);
    }

    @Override
    public long countByUser() throws CustomException {
        User user = userService.getCurrentUser();
        if(user == null) throw new CustomException("User not found");
        return notificationDao.countBy("user", user);
    }

    @Override
    public void delete(int id) throws CustomException {
        Notification notification = notificationDao.getByKey(id);
        if(notification == null) throw new CustomException("Notification not found");
        notificationDao.delete(notification);
    }
}
