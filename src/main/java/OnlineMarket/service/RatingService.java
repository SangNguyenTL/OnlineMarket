package OnlineMarket.service;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Product;
import OnlineMarket.model.Rating;
import OnlineMarket.model.User;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.product.ProductNotFoundException;
import OnlineMarket.util.exception.rating.RatingNotFoundException;
import OnlineMarket.util.exception.user.UserNotFoundException;

import java.util.List;


public interface RatingService {
    ResultObject<Rating> listByProduct(Product product, FilterForm filterForm) throws ProductNotFoundException;
    ResultObject<Rating> listByUserKey(User user, FilterForm filterForm);
    void save(Rating rating) throws UserNotFoundException, ProductNotFoundException;
    Rating getByKey(Integer key);
    Rating getByUserIdAndProductId(int userId, int productId);
    ResultObject<Rating> list(FilterForm filterForm);

    List<Rating> list(User user);

    void update(Rating entity) throws RatingNotFoundException;

    void delete(Integer id) throws RatingNotFoundException;

    void delete(Rating rating);

    Rating activeRating(int id) throws RatingNotFoundException;
}
