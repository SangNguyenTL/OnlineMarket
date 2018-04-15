package onlinemarket.service;

import onlinemarket.dao.RatingDao;
import onlinemarket.dao.RatingStatisticDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Product;
import onlinemarket.model.Rating;
import onlinemarket.model.RatingStatistic;
import onlinemarket.model.User;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.product.ProductNotFoundException;
import onlinemarket.util.exception.rating.RatingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("ratingService")
@Transactional
public class RatingServiceImpl implements RatingService{

    @Autowired
    RatingDao ratingDao;

    @Autowired
    RatingStatisticDao ratingStatisticDao;

    @Override
    public ResultObject<Rating> listByProduct(Product product, FilterForm filterForm) throws  ProductNotFoundException {
        if (product == null) throw new ProductNotFoundException();
        return ratingDao.listByDeclaration("product", product, filterForm);
    }

    @Override
    public void save(Rating rating, User user) {

        rating.setCreateRateDate(new Date());
        rating.setState("Inactive");
        rating.setUser(user);
        ratingDao.persist(rating);

    }

    @Override
    public ResultObject<Rating> list(FilterForm filterForm) {
        return ratingDao.list(filterForm);
    }

    @Override
    public void update(Rating entity) throws RatingNotFoundException {
        Rating ratingCheck = ratingDao.getByKey(entity.getId());
        if (ratingCheck == null)
            throw new RatingNotFoundException();
        ratingCheck.setState(entity.getState());
        ratingCheck.setContent(entity.getContent());
        ratingCheck.setScore(entity.getScore());
        ratingCheck.setRateDate(new Date());
        ratingDao.update(ratingCheck);
        RatingStatistic ratingfStatistic = ratingStatisticDao.getByKey(ratingCheck.getProduct().getId());
        if(ratingCheck.getState().equals("Active")){
            if(ratingfStatistic == null)  ratingfStatistic = new RatingStatistic();
            ratingfStatistic.setTotalScore(ratingfStatistic.getTotalScore() + ratingCheck.getScore());
            ratingfStatistic.setUserCount(ratingfStatistic.getUserCount() + 1);
            ratingfStatistic.setAverageScore(ratingfStatistic.getTotalScore() / ratingfStatistic.getUserCount());
            ratingfStatistic.setProduct(ratingCheck.getProduct());
        }else{
            ratingfStatistic.setTotalScore(ratingfStatistic.getTotalScore() - ratingCheck.getScore());
            ratingfStatistic.setUserCount(ratingfStatistic.getUserCount() - 1);
            ratingfStatistic.setAverageScore(ratingfStatistic.getTotalScore() / ratingfStatistic.getUserCount());
            ratingfStatistic.setProduct(ratingCheck.getProduct());
        }
        ratingStatisticDao.update(ratingfStatistic);
    }

    @Override
    public void delete(Integer id) throws RatingNotFoundException {
        Rating rating = ratingDao.getByKey(id);
        if (rating == null)
            throw new RatingNotFoundException();
        ratingDao.delete(rating);
    }

    @Override
    public Rating getByKey(Integer key) {
        return ratingDao.getByKey(key);
    }
}
