package OnlineMarket.service;

import OnlineMarket.dao.RatingDao;
import OnlineMarket.dao.RatingStatisticDao;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Product;
import OnlineMarket.model.Rating;
import OnlineMarket.model.RatingStatistic;
import OnlineMarket.model.User;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.product.ProductNotFoundException;
import OnlineMarket.util.exception.rating.RatingNotFoundException;
import OnlineMarket.util.exception.user.UserNotFoundException;
import OnlineMarket.util.other.State;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("ratingService")
@Transactional
public class RatingServiceImpl implements RatingService{

    @Autowired
    RatingDao ratingDao;

    @Autowired
    RatingStatisticDao ratingStatisticDao;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Override
    public ResultObject<Rating> listByProduct(Product product, FilterForm filterForm) throws  ProductNotFoundException {
        if (product == null) throw new ProductNotFoundException();
        return ratingDao.listByDeclaration("product", product, filterForm);
    }

    @Override
    public ResultObject<Rating> listByUserKey(User user, FilterForm filterForm) {
        return ratingDao.listByDeclaration("user", user, filterForm);
    }


    @Override
    public void save(Rating rating) throws UserNotFoundException, ProductNotFoundException {
        if(rating.getUser()!=null && userService.getByKey(rating.getUser().getId()) == null) throw new UserNotFoundException();
        if(rating.getProduct() !=null && productService.getByKey(rating.getProduct().getId()) == null) throw new ProductNotFoundException();
        Document doc = Jsoup.parse(rating.getContent());

        doc = new Cleaner(Whitelist.simpleText()).clean(doc);

        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);

        rating.setContent(doc.body().html());
        rating.setCreateRateDate(new Date());
        rating.setState(State.INACTIVE.name());
        ratingDao.persist(rating);
    }

    @Override
    public ResultObject<Rating> list(FilterForm filterForm) {
        return ratingDao.list(filterForm);
    }

    @Override
    public List<Rating> list(User user){
        return ratingDao.listByDeclaration("user", user);
    }

    @Override
    public void update(Rating entity) throws RatingNotFoundException {
        Rating ratingCheck = ratingDao.getByKey(entity.getId());
        if (ratingCheck == null) throw new RatingNotFoundException();
        entity.setUpdateDate(new Date());
        entity.setCreateRateDate(ratingCheck.getCreateRateDate());
        ratingDao.merge(entity);
    }

    @Override
    public void delete(Integer id) throws RatingNotFoundException {
        Rating rating = ratingDao.getByKey(id);
        if (rating == null)
            throw new RatingNotFoundException();
        rating.setState(State.INACTIVE.name());
        updateRatingStatistic(rating);
        ratingDao.delete(rating);
    }

    @Override
    public void delete(Rating rating) {
        rating.setState(State.INACTIVE.name());
        updateRatingStatistic(rating);
        ratingDao.delete(rating);
    }

    @Override
    public Rating getByKey(Integer key) {
        return ratingDao.getByKey(key);
    }

    @Override
    public Rating getByUserIdAndProductId(int userId, int productId) {
       return ratingDao.getByUserIdAndProductId(userId,productId);
    }

    @Override
    public Rating activeRating(int id) throws RatingNotFoundException {
        Rating rating = ratingDao.getByKey(id);
        if(rating == null) throw new RatingNotFoundException();
        rating.setState(State.ACTIVE.name());
        rating.setUpdateDate(new Date());
        ratingDao.merge(rating);
        updateRatingStatistic(rating);
        return rating;
    }

    private void updateRatingStatistic(Rating rating){

        RatingStatistic ratingStatistic = ratingStatisticDao.getByDeclaration("product", rating.getProduct());
        if(rating.getState().equals(State.ACTIVE.name())){
            if(ratingStatistic == null)  {
                ratingStatistic = new RatingStatistic();
                ratingStatistic.setProduct(rating.getProduct());
            }
            ratingStatistic.setTotalScore(ratingStatistic.getTotalScore() + rating.getScore());
            ratingStatistic.setUserCount(ratingStatistic.getUserCount() + 1);
            if(ratingStatistic.getUserCount() > 0)
            ratingStatistic.setAverageScore((double) ratingStatistic.getTotalScore() / ratingStatistic.getUserCount());

        }else{
            if(ratingStatistic == null) return;
            ratingStatistic.setTotalScore(ratingStatistic.getTotalScore() - rating.getScore());
            ratingStatistic.setUserCount(ratingStatistic.getUserCount() - 1);
            if(ratingStatistic.getUserCount() > 0)
            ratingStatistic.setAverageScore((double) ratingStatistic.getTotalScore() / ratingStatistic.getUserCount());
            else ratingStatistic.setAverageScore(0);

        }

        ratingStatisticDao.merge(ratingStatistic);
    }
}
