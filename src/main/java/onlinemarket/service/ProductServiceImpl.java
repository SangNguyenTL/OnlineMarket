package onlinemarket.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import onlinemarket.dao.AttributeValuesDao;
import onlinemarket.dao.CommentDao;
import onlinemarket.form.filter.SearchSelect;
import onlinemarket.model.*;
import onlinemarket.result.api.Pagination;
import onlinemarket.result.api.ResultProduct;
import onlinemarket.util.Slugify;
import onlinemarket.util.exception.product.ProductHasCommentException;
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
    CommentDao commentDao;

    @Autowired
    AttributeValuesDao attributeValuesDao;

    @Autowired
    Slugify slugify;

    @Override
    public Product getByKey(Integer key) {
        return productDao.getByKey(key);
    }

    @Override
    public Product getByDeclaration(String key, Object value) {
        return productDao.getByDeclaration(key, value);
    }

    private List<ProductAttributeValues> makeListProductAttributeValues(Product newProduct, List<ProductAttributeValues> inputList){
        List<ProductAttributeValues> productAttributeValuesList = new ArrayList<>();
        for (ProductAttributeValues productAttributeValues : inputList) {
            if (productAttributeValues.getAttributeValuesId() != null) {
                AttributeValues attributeValues = attributeValuesDao.getByKey(productAttributeValues.getAttributeValuesId());
                if (attributeValues != null) {
                    ProductAttributeValues productAttributeValues1 = new ProductAttributeValues();
                    productAttributeValues1.setProductId(newProduct.getId());
                    productAttributeValues1.setAttributeValuesId(attributeValues.getId());
                    productAttributeValuesList.add(productAttributeValues1);
                }
            }
        }
        return productAttributeValuesList;
    }

    @Override
    public void save(Product product, ProductCategory productCategory, User user) throws ProductCategoryNotFoundException {
        if (productCategory == null) throw new ProductCategoryNotFoundException();
        product.setProductCategory(productCategory);
        product.setCreateDate(new Date());
        product.setUser(user);
        product.setProductCategory(productCategory);
        product.setSlug(slugify.slugify(product.getSlug()));
        List<ProductAttributeValues> productAttributeValuesList = new ArrayList<>(product.getProductAttributeValues());
        product.getProductAttributeValues().clear();
        productDao.persist(product);
        product.setProductAttributeValues(makeListProductAttributeValues(product, productAttributeValuesList));
        productDao.update(product);
    }

    @Override
    public void update(Product product, ProductCategory productCategory, User user) throws ProductCategoryNotFoundException, ProductNotFoundException {
        if (productCategory == null) throw new ProductCategoryNotFoundException();
        Product product1 = productDao.getByKey(product.getId());
        if (product1 == null) throw new ProductNotFoundException();
        product.setUser(user);
        product.setProductCategory(productCategory);
        product.setSlug(slugify.slugify(product.getSlug()));
        product1.getProductAttributeValues().clear();
        product1.updateProduct(product);
        product1.getProductAttributeValues().addAll(makeListProductAttributeValues(product1, product.getProductAttributeValues()));
        productDao.update(product1);
    }

    @Override
    public void delete(Integer id) throws ProductNotFoundException, ProductHasCommentException {
        Product product = productDao.getByKey(id);
        if(product == null) throw new ProductNotFoundException();
        if(commentDao.getUniqueResultBy("products.id", product.getId()) != null) throw new ProductHasCommentException();
        productDao.delete(product);
    }

    @Override
    public ResultObject<Product> listByProductCategory(ProductCategory productCategory, FilterForm filterForm) throws ProductCategoryNotFoundException {
        if (productCategory == null) throw new ProductCategoryNotFoundException();
        return productDao.listByDeclaration("productCategory", productCategory, filterForm);
    }

    @Override
    public ResultObject<Product> list(FilterForm filterForm) {
        return productDao.list(filterForm);
    }

    @Override
    public ResultProduct searchSelect(SearchSelect searchSelect) {
        FilterForm filterForm = new FilterForm();
        filterForm.setSearch(searchSelect.getQ());
        filterForm.setCurrentPage(searchSelect.getPage());
        ResultObject<Product> productResultObject = productDao.list(filterForm);
        ResultProduct resultProduct = new ResultProduct();
        resultProduct.setResults(productResultObject.getList());
        resultProduct.setPagination(new Pagination(productResultObject.getCurrentPage() < productResultObject.getTotalPages()));
        return resultProduct;
    }

    @Override
    public List<Product> getRelatedProduct(ProductCategory productCategory, Brand brand) {
        return productDao.getRelatedProduct(productCategory,brand);
    }

}
