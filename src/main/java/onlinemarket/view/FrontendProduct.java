package onlinemarket.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import onlinemarket.model.*;
import onlinemarket.util.Help;

import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class FrontendProduct extends Product {

    private String sale = "";
    private long newPrice;
    private String priceStr;
    private String newPriceStr;

    public FrontendProduct() {
        super();
    }

    public FrontendProduct(Integer id, Brand brand, ProductCategory productCategory, User user, String name, String slug, String description, long price, Integer quantity, Integer numberOrder, Byte state, Integer weight, Date releaseDate, String size, Date createDate, Date updateDate, String featureImage, RatingStatistic ratingStatistic, ProductViewsStatistic productViewsStatistic, Set<Rating> ratings, Set<Event> events, List<ProductAttributeValues> productAttributeValues, Set<ProductViews> productViewses, Set<Comment> comments, Set<Cart> carts) {
        super(id, brand, productCategory, user, name, slug, description, price, quantity, numberOrder, state, weight, releaseDate, size, createDate, updateDate, featureImage, ratingStatistic, productViewsStatistic, ratings, events, productAttributeValues, productViewses, comments, carts);
    }

    public boolean checkProductAttributes(AttributeValues attributeValues){
        ProductAttributeValues productAttributeValue = new ProductAttributeValues();
        productAttributeValue.setAttributeValuesId(attributeValues.getId());
        productAttributeValue.setProductId(this.getId());
        return productAttributeValues.contains(productAttributeValue);
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public long getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(long newPrice) {
        this.newPrice = newPrice;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }

    public String getNewPriceStr() {
        return newPriceStr;
    }

    public void setNewPriceStr(String newPriceStr) {
        this.newPriceStr = newPriceStr;
    }
}
