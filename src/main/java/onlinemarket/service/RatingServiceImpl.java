package onlinemarket.service;

import onlinemarket.dao.RatingDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Product;
import onlinemarket.model.Rating;
import onlinemarket.model.User;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.product.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("ratingService")
@Transactional
public class RatingServiceImpl implements RatingService{

    @Autowired
    RatingDao ratingDao;


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
}
