package onlinemarket.service;

import onlinemarket.dao.ProductViewsDao;
import onlinemarket.dao.ProductViewsStatisticDao;
import onlinemarket.model.Product;
import onlinemarket.model.ProductViews;
import onlinemarket.model.ProductViewsStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service("productViewsService")
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

        Calendar cSource = Calendar.getInstance();
        cSource.set(Calendar.DAY_OF_WEEK, 1);
        cSource.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cSource.clear(Calendar.MINUTE);
        cSource.clear(Calendar.SECOND);
        cSource.clear(Calendar.MILLISECOND);

        Calendar cCurrent = Calendar.getInstance();
        cCurrent.setTime(new Date());
        cCurrent.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cCurrent.clear(Calendar.MINUTE);
        cCurrent.clear(Calendar.SECOND);
        cCurrent.clear(Calendar.MILLISECOND);

        if(cCurrent.getTime().equals(cSource.getTime()))
            productViewsStatistic.setWeek(productViews.getCount());
        else
            productViewsStatistic.setWeek(productViewsStatistic.getWeek()+1);
        cSource.set(Calendar.DAY_OF_MONTH, 1);

        if(cCurrent.getTime().equals(cSource.getTime()))
            productViewsStatistic.setMonth(productViews.getCount());
        else
            productViewsStatistic.setMonth(productViewsStatistic.getMonth()+1);

        cSource.set(Calendar.DAY_OF_YEAR, 1);
        if(cCurrent.getTime().equals(cSource.getTime()))
            productViewsStatistic.setYear(productViews.getCount());
        else
            productViewsStatistic.setYear(productViews.getCount());

        productViewsStatistic.setTotal(productViewsStatistic.getTotal()+1);

        productViewsDao.update(productViews);

        productViewsStatisticDao.update(productViewsStatistic);
    }

}
