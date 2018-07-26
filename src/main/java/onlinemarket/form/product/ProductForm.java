package onlinemarket.form.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import onlinemarket.model.*;
import onlinemarket.model.other.AdvancedValidation;
import onlinemarket.validation.UniqueProductSlug;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.Set;

@UniqueProductSlug(groups = {AdvancedValidation.CheckSlug.class})
public class ProductForm extends Product {

    private String beforeSlug;

    private Integer countAttribute = -1;

    public ProductForm() {
        super();
    }

    public ProductForm(Integer id, Brand brand, ProductCategory productCategory, User user, String name, String slug, String description, long price, Integer quantity, Integer numberOrder, Byte state, Integer weight, Date releaseDate, String size, Date createDate, Date updateDate, String featureImage, RatingStatistic ratingStatistic, ProductViewsStatistic productViewsStatistic, Set<Rating> ratings, Set<Event> events, List<ProductAttributeValues> productAttributeValues, Set<ProductViews> productViewses, Set<Comment> comments, Set<Cart> carts) {
        super(id, brand, productCategory, user, name, slug, description, price, quantity, numberOrder, state, weight, releaseDate, size, createDate, updateDate, featureImage, ratingStatistic, productViewsStatistic, ratings, events, productAttributeValues, productViewses, comments, carts);
    }

    public String getBeforeSlug() {
        return beforeSlug;
    }

    public void setBeforeSlug(String beforeSlug) {
        this.beforeSlug = beforeSlug;
    }

    public Integer getCountAttribute() {
        return countAttribute;
    }

    public void setCountAttribute(Integer countAttribute) {
        this.countAttribute = countAttribute;
    }

    public Integer processCount(){
        return countAttribute++;
    }

    public boolean checkProductAttributes(AttributeValues attributeValues){
        ProductAttributeValues productAttributeValue = new ProductAttributeValues();
        productAttributeValue.setAttributeValuesId(attributeValues.getId());
        productAttributeValue.setProductId(this.getId());
        return productAttributeValues.contains(productAttributeValue);
    }
}
