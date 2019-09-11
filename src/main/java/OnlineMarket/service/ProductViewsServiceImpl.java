package OnlineMarket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import OnlineMarket.dao.ProductViewsDao;
import OnlineMarket.dao.ProductViewsStatisticDao;
import OnlineMarket.model.Product;
import OnlineMarket.model.ProductViews;
import OnlineMarket.model.ProductViewsStatistic;

@Service("productViewsService")
@Transactional
public class ProductViewsServiceImpl implements ProductViewsService{

    @Autowired
    ProductViewsDao productViewsDao;

    @Autowired
    ProductViewsStatisticDao productViewsStatisticDao;

    @Override
    public ProductViews getByProductInCurrentDay(Product product) {
        return productViewsDao.getByProductInCurrentDay(product);
    }

    @Override
    public void save(Product product) {
        ProductViews productViews = productViewsDao.getByProductInCurrentDay(product);
        if(productViews == null){
            productViews = new ProductViews();
            productViews.setProduct(product);
        }
        productViews.setCount(productViews.getCount()+1);

        ProductViewsStatistic productViewsStatistic = productViewsStatisticDao.getByDeclaration("product", product);
        if(productViewsStatistic == null) {
            productViewsStatistic = new ProductViewsStatistic();
            productViewsStatistic.setProduct(product);
        }
        productViewsStatistic.setWeek(productViewsStatistic.getWeek()+1);

        productViewsStatistic.setMonth(productViewsStatistic.getMonth()+1);

        productViewsStatistic.setYear(productViewsStatistic.getYear()+1);

        productViewsStatistic.setTotal(productViewsStatistic.getTotal()+1);

        productViewsDao.merge(productViews);

        productViewsStatisticDao.merge(productViewsStatistic);
    }

}
