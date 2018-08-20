package OnlineMarket.service;

import java.util.Date;
import java.util.List;

import OnlineMarket.dao.AttributeGroupDao;
import OnlineMarket.dao.EventDao;
import OnlineMarket.dao.ProductDao;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.Slugify;
import OnlineMarket.util.exception.productCategory.ProductCategoryHasAttributeGroupException;
import OnlineMarket.util.exception.productCategory.ProductCategoryHasProductException;
import OnlineMarket.util.exception.productCategory.ProductCategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import OnlineMarket.dao.ProductCategoryDao;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Event;
import OnlineMarket.model.ProductCategory;
import OnlineMarket.result.ResultObject;

@Service("productCategoryService")
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    ProductCategoryDao productCategoryDao;

    @Autowired
    EventDao eventDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    AttributeGroupDao attributeGroupDao;

    @Autowired
    Slugify slg;

    @Override
    public void save(ProductCategory entity) {

        entity.setSlug(slg.slugify(entity.getSlug()));
        productCategoryDao.save(entity);

    }

    @Override
    public void update(ProductCategory entity) throws CustomException {

        ProductCategory productCategoryCheck = productCategoryDao.getByKey(entity.getId());
        if (productCategoryCheck == null)
            throw new CustomException("Product category not found");
        entity.setSlug(slg.slugify(entity.getSlug()));
        entity.setUpdateDate(new Date());
        entity.setCreateDate(productCategoryCheck.getCreateDate());
        productCategoryDao.merge(entity);

    }

    @Override
    public void delete(ProductCategory entity) {
        productCategoryDao.delete(entity);
    }

    @Override
    public ProductCategory getByKey(Integer key) {
        return productCategoryDao.getByKey(key);
    }

    @Override
    public ProductCategory getByDeclaration(String key, Object value) {
        return productCategoryDao.getByDeclaration(key, value);
    }

    @Override
    public List<ProductCategory> list() {
        return productCategoryDao.list();
    }

    @Override
    public List<ProductCategory> list(Integer offset, Integer maxResults) {
        return productCategoryDao.list(offset, maxResults);
    }

    @Override
    public ResultObject<ProductCategory> list(FilterForm filterForm) {
        return productCategoryDao.list(filterForm);
    }

    @Override
    public ProductCategory getByEvent(Event event) {
        return productCategoryDao.getByEvent(event);
    }

    @Override
    public void delete(Integer id) throws ProductCategoryNotFoundException, ProductCategoryHasProductException, ProductCategoryHasAttributeGroupException {

        ProductCategory productCategory = productCategoryDao.getByKey(id);
        if(productCategory == null) throw new ProductCategoryNotFoundException();
        if(productDao.getUniqueResultBy("productCategory", productCategory) != null) throw new ProductCategoryHasProductException();
        if(attributeGroupDao.getUniqueResultBy("productCategory", productCategory) != null) throw new ProductCategoryHasAttributeGroupException();
        productCategoryDao.delete(productCategory);

    }
}
