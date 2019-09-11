package OnlineMarket.form.product;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import OnlineMarket.model.AttributeValues;
import OnlineMarket.model.Product;
import OnlineMarket.model.ProductAttributeValues;
import OnlineMarket.util.group.AdvancedValidation;
import OnlineMarket.validation.UniqueProductSlug;

@UniqueProductSlug(groups = {AdvancedValidation.CheckSlug.class})
public class ProductForm extends Product {

    private static final long serialVersionUID = 1L;

    private String beforeSlug;

    private Integer countAttribute = -1;

    public ProductForm() {
        super();
    }

    public ProductForm(Product parent){
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
