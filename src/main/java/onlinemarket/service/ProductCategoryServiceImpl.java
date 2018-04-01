package onlinemarket.service;

import java.util.Date;
import java.util.List;

import onlinemarket.util.exception.CustomException;
import onlinemarket.util.Slugify;
import onlinemarket.util.exception.productCategory.ProductCategoryHasAttributeGroup;
import onlinemarket.util.exception.productCategory.ProductCategoryHasEvent;
import onlinemarket.util.exception.productCategory.ProductCategoryHasProduct;
import onlinemarket.util.exception.productCategory.ProductCategoryNotFound;
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

    protected Slugify slg;

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
    public void delete(Integer id) throws ProductCategoryNotFound, ProductCategoryHasProduct, ProductCategoryHasEvent, ProductCategoryHasAttributeGroup {

        ProductCategory productCategory = productCategoryDao.getByKey(id);
        if(productCategory == null) throw new ProductCategoryNotFound();
        if(!productCategory.getProducts().isEmpty()) throw new ProductCategoryHasProduct();
        if(!productCategory.getEvents().isEmpty()) throw new ProductCategoryHasEvent();
        if(!productCategory.getAttributeGroups().isEmpty()) throw new ProductCategoryHasAttributeGroup();
        productCategoryDao.delete(productCategory);

    }
}
