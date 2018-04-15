package onlinemarket.service;

import onlinemarket.dao.RatingDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Product;
import onlinemarket.model.Rating;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.product.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

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
}
