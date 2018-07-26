package onlinemarket.service;

import java.util.Date;
import java.util.List;

import onlinemarket.dao.AttributeGroupDao;
import onlinemarket.dao.EventDao;
import onlinemarket.dao.ProductDao;
import onlinemarket.util.exception.CustomException;
import onlinemarket.util.Slugify;
import onlinemarket.util.exception.productCategory.ProductCategoryHasAttributeGroupException;
import onlinemarket.util.exception.productCategory.ProductCategoryHasEventException;
import onlinemarket.util.exception.productCategory.ProductCategoryHasProductException;
import onlinemarket.util.exception.productCategory.ProductCategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.ProductCategoryDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Event;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;

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
        productCategoryDao.update(entity);

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
    public ProductCategory getByDeclaration(String key, String value) {
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
    public void delete(Integer id) throws ProductCategoryNotFoundException, ProductCategoryHasProductException, ProductCategoryHasEventException, ProductCategoryHasAttributeGroupException {

        ProductCategory productCategory = productCategoryDao.getByKey(id);
        if(productCategory == null) throw new ProductCategoryNotFoundException();
        if(productDao.getUniqueResultBy("productCategory", productCategory) != null) throw new ProductCategoryHasProductException();
        if(eventDao.getUniqueResultBy("productCategory", productCategory) != null) throw new ProductCategoryHasEventException();
        if(attributeGroupDao.getUniqueResultBy("productCategory", productCategory) != null) throw new ProductCategoryHasAttributeGroupException();
        productCategoryDao.delete(productCategory);

    }
}
