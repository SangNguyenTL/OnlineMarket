package OnlineMarket.service;

import java.util.*;

import OnlineMarket.dao.AttributeValuesDao;
import OnlineMarket.dao.CommentDao;
import OnlineMarket.form.filter.SearchSelect;
import OnlineMarket.form.product.ProductForm;
import OnlineMarket.model.*;
import OnlineMarket.result.api.Pagination;
import OnlineMarket.result.api.ResultProduct;
import OnlineMarket.util.Help;
import OnlineMarket.util.Slugify;
import OnlineMarket.util.exception.product.ProductHasCommentException;
import OnlineMarket.util.exception.product.ProductNotFoundException;
import OnlineMarket.util.exception.productCategory.ProductCategoryNotFoundException;
import OnlineMarket.view.FrontendProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import OnlineMarket.dao.ProductDao;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.result.ResultObject;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

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

    @Override
    public FrontendProduct getFrontendProductByDeclaration(String key, Object value){
        return convertProductToFrProduct(getByDeclaration(key,value));
    }

    private List<ProductAttributeValues> makeListProductAttributeValues(Product newProduct, List<ProductAttributeValues> inputList){
        List<ProductAttributeValues> productAttributeValuesList = new ArrayList<>();
        for (ProductAttributeValues productAttributeValues : inputList) {
            if (productAttributeValues.getAttributeValuesId() != null) {
                AttributeValues attributeValues = attributeValuesDao.getByKey(productAttributeValues.getAttributeValuesId());
                if (attributeValues != null) {
                    if(newProduct.getId() == null)
                        productAttributeValues.setProduct(newProduct);
                    else productAttributeValues.setProductId(newProduct.getId());
                    productAttributeValues.setAttributeValuesId(attributeValues.getId());
                    productAttributeValuesList.add(productAttributeValues);
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
        if(product instanceof ProductForm) product = new Product((ProductForm) product);
        List<ProductAttributeValues> productAttributeValuesList = new ArrayList<>(product.getProductAttributeValues());
        product.getProductAttributeValues().clear();
        productDao.persist(product);
        product.setProductAttributeValues(makeListProductAttributeValues(product, productAttributeValuesList));
        if(product.getProductAttributeValues().size() > 0)
        productDao.merge(product);
    }

    @Override
    public void update(Product product, ProductCategory productCategory, User user) throws ProductCategoryNotFoundException, ProductNotFoundException {
        if (productCategory == null) throw new ProductCategoryNotFoundException();
        Product product1 = productDao.getByKey(product.getId());
        if (product1 == null) throw new ProductNotFoundException();
        product.setUser(user);
        product.setProductCategory(productCategory);
        product.setSlug(slugify.slugify(product.getSlug()));
        List<ProductAttributeValues> productAttributeValuesList = new ArrayList<>(product.getProductAttributeValues());
        product.getProductAttributeValues().clear();
        product.getProductAttributeValues().addAll(makeListProductAttributeValues(product1, productAttributeValuesList));
        product.setUpdateDate(new Date());
        product.setCreateDate(product1.getCreateDate());
        if(product instanceof ProductForm) product = new Product((ProductForm) product);
        productDao.merge(product);
    }

    @Override
    public void delete(Integer id) throws ProductNotFoundException {
        Product product = productDao.getByKey(id);
        if(product == null) throw new ProductNotFoundException();
        productDao.delete(product);
    }

    @Override
    public ResultObject<Product> listByProductCategory(ProductCategory productCategory, FilterForm filterForm) throws ProductCategoryNotFoundException {
        if (productCategory == null) throw new ProductCategoryNotFoundException();
        return productDao.listByDeclaration("productCategory", productCategory, filterForm);
    }

    @Override
    public ResultObject<FrontendProduct> frontendProductResultObject(ResultObject<Product> productResultObject) {

        ResultObject<FrontendProduct> frontendProductResultObject = new ResultObject<>();
        frontendProductResultObject.setTotalPages(productResultObject.getTotalPages());
        frontendProductResultObject.setCurrentPage(productResultObject.getCurrentPage());
        frontendProductResultObject.setList(convertProductToFrProduct(productResultObject.getList()));

        return frontendProductResultObject;
    }

    @Override
    public ResultObject<Product> list(FilterForm filterForm) {
        return productDao.list(filterForm);
    }

    @Override
    public ResultObject<Product> listByInList(String key, List<Object> listValue, FilterForm filterForm) {
        return productDao.listByInList(key, listValue, filterForm);
    }

    @Override
    public ResultProduct searchSelect(SearchSelect searchSelect) {
        FilterForm filterForm = new FilterForm();
        filterForm.setSearch(searchSelect.getQ());
        filterForm.setSearchBy("name");
        filterForm.setCurrentPage(searchSelect.getPage());
        ResultObject<Product> productResultObject = productDao.list(filterForm);
        ResultProduct resultProduct = new ResultProduct();
        resultProduct.setResults(productResultObject.getList());
        resultProduct.setPagination(new Pagination(productResultObject.getCurrentPage() < productResultObject.getTotalPages()));
        return resultProduct;
    }

    @Override
    public List<Product> getRelatedProduct(ProductCategory productCategory, Brand brand, Product product) {
        return productDao.getRelatedProduct(productCategory,brand, product);
    }

    @Override
    public List<FrontendProduct> convertProductToFrProduct(List<Product> productList) {
        List<FrontendProduct> frontendProductList = new ArrayList<>();
        for(Product product : productList){
            frontendProductList.add(convertProductToFrProduct(product));
        }
        return frontendProductList;
    }

    @Override
    public FrontendProduct convertProductToFrProduct(Product product){
        if(product == null) return null;
        FrontendProduct frontendProduct = new FrontendProduct(product);

        frontendProduct.setNewPrice(product.getPrice());
        frontendProduct.setPriceStr(Help.format(product.getPrice()));

        Set<Event> events =  frontendProduct.getEvents();
        processEventProduct(frontendProduct, events);

        return frontendProduct;
    }

    @Override
    public void processEventProduct(FrontendProduct frontendProduct, Set<Event> events){
        Integer perSale = 0;
        long value = 0;
        for (Event event : events){
            if(event.getMaxPrice() >= frontendProduct.getPrice() && event.getMinPrice() <= frontendProduct.getPrice()){
                if(event.getPercentValue() != null)
                    perSale += event.getPercentValue();
                if(event.getValue() != null)
                    value += event.getValue();
            }else events.remove(event);
        }

        Double number = Math.floor(frontendProduct.getPrice() - (frontendProduct.getPrice() * perSale/100d) - value);
        if(number <0) number = 0d;

        frontendProduct.setNewPrice(number.longValue());
        frontendProduct.setNewPriceStr(Help.format(frontendProduct.getNewPrice()));
        frontendProduct.setEvents(events);

        if(perSale != 0)
            frontendProduct.setSale("-"+perSale.toString()+"%");
        else if(value != 0) frontendProduct.setSale("-"+Help.format(value));
    }
}
