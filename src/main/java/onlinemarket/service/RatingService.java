package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Product;
import onlinemarket.model.Rating;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.product.ProductNotFoundException;


public interface RatingService {
    ResultObject<Rating> listByProduct(Product product, FilterForm filterForm) throws ProductNotFoundException;
}
