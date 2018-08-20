package OnlineMarket.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ProductAttributeValuesId implements Serializable {


    private static final long serialVersionUID = 1L;

    private Integer attributeValuesId;
    private Integer productId;

    @Column(name = "attribute_values_id", nullable = false)
    @Id
    public Integer getAttributeValuesId() {
        return attributeValuesId;
    }

    public void setAttributeValuesId(Integer attributeValuesId) {
        this.attributeValuesId = attributeValuesId;
    }

    @Column(name = "product_id", nullable = false)
    @Id
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductAttributeValuesId that = (ProductAttributeValuesId) o;
        return Objects.equals(attributeValuesId, that.attributeValuesId) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(attributeValuesId, productId);
    }
}
