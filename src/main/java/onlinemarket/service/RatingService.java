package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Product;
import onlinemarket.model.Rating;
import onlinemarket.model.User;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.product.ProductNotFoundException;
import onlinemarket.util.exception.rating.RatingNotFoundException;


public interface RatingService {
    ResultObject<Rating> listByProduct(Product product, FilterForm filterForm) throws ProductNotFoundException;
    void save(Rating rating, User user) throws RatingNotFoundException;
}
