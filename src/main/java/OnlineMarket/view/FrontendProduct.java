package OnlineMarket.view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import OnlineMarket.model.AttributeValues;
import OnlineMarket.model.Product;
import OnlineMarket.model.ProductAttributeValues;

public class FrontendProduct extends Product {

    private static final long serialVersionUID = 1L;

    private String sale = "";
    private long newPrice;
    private String priceStr;
    private String newPriceStr;

    public FrontendProduct(FrontendProduct frontendProduct){
        for (Method getMethod : frontendProduct.getClass().getMethods()) {
            if (getMethod.getName().startsWith("get")) {
                try {
                    Method setMethod = this.getClass().getMethod(getMethod.getName().replace("get", "set"), getMethod.getReturnType());
                    setMethod.invoke(this, getMethod.invoke(frontendProduct, (Object[]) null));

                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ignore) {

                }
            }
        }
    }

    public FrontendProduct(Product parent){
        for (Method getMethod : parent.getClass().getMethods()) {
            if (getMethod.getName().startsWith("get")) {
                try {
                    Method setMethod = this.getClass().getMethod(getMethod.getName().replace("get", "set"), getMethod.getReturnType());
                    setMethod.invoke(this, getMethod.invoke(parent, (Object[]) null));

                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ignore) {

                }
            }
        }
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
