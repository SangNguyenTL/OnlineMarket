package onlinemarket.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import onlinemarket.dao.AttributeValuesDao;
import onlinemarket.model.*;
import onlinemarket.util.exception.CustomException;
import onlinemarket.util.exception.product.ProductNotFoundException;
import onlinemarket.util.exception.productCategory.ProductCategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.ProductDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.result.ResultObject;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Autowired
    AttributeValuesDao attributeValuesDao;

    @Override
    public Product getByKey(Integer key) {
        return productDao.getByKey(key);
    }

    @Override
    public Product getByDeclaration(String key, Object value) {
        return productDao.getByDeclaration(key, value);
    }

    @Override
    public void save(Product product, ProductCategory productCategory, User user) throws ProductCategoryNotFoundException {
        if (productCategory == null) throw new ProductCategoryNotFoundException();
        product.setProductCategory(productCategory);
        product.setCreateDate(new Date());
        product.setUser(user);
        productDao.save(product);
    }

    @Override
    public void update(Product product, ProductCategory productCategory, User user) throws ProductCategoryNotFoundException, ProductNotFoundException {
        if (productCategory == null) throw new ProductCategoryNotFoundException();
        Product product1 = productDao.getByKey(product.getId());
        if (product1 == null) throw new ProductNotFoundException();
        product.setUser(user);
        product.setProductCategory(productCategory);
        product1.getProductAttributeValues().clear();
        List<ProductAttributeValues> productAttributeValuesList = new ArrayList<>();
        for(int i = 0; i< product.getProductAttributeValues().size(); i++){
            ProductAttributeValues productAttributeValues = product.getProductAttributeValues().get(i);
            if(productAttributeValues.getAttributeValuesId()!= null) {
                AttributeValues attributeValues = attributeValuesDao.getByKey(productAttributeValues.getAttributeValuesId());
                if(attributeValues != null){
                    ProductAttributeValues productAttributeValues1 = new ProductAttributeValues();
                    productAttributeValues1.setProductId(product.getId());
                    productAttributeValues1.setAttributeValuesId(attributeValues.getId());
                    productAttributeValuesList.add(productAttributeValues1);
                }
            }
        }
        product1.updateProduct(product);
        product1.getProductAttributeValues().addAll(productAttributeValuesList);
        productDao.update(product1);
    }

    @Override
    public ResultObject<Product> listByProductCategory(ProductCategory productCategory, FilterForm filterForm) throws ProductCategoryNotFoundException {
        if (productCategory == null) throw new ProductCategoryNotFoundException();
        return productDao.listByDeclaration("productCategory", productCategory, filterForm);
    }

}
