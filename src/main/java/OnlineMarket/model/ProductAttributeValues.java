package OnlineMarket.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tb_product_attribute_values", schema = "dbo", catalog = "SmartMarket")
@IdClass(ProductAttributeValuesId.class)
public class ProductAttributeValues {

    private static final long serialVersionUID = 1L;

    private Integer attributeValuesId;
    private Integer productId;
    private AttributeValues attributeValues;
    private Product product;

    @Id
    @Column(name = "attribute_values_id", nullable = false)
    public Integer getAttributeValuesId() {
        return attributeValuesId;
    }

    public void setAttributeValuesId(Integer attributeValuesId) {
        this.attributeValuesId = attributeValuesId;
    }

    @Id
    @Column(name = "product_id", nullable = false)
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
        ProductAttributeValues that = (ProductAttributeValues) o;
        return Objects.equals(attributeValuesId, that.attributeValuesId) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(attributeValuesId, productId);
    }

    @ManyToOne
    @JoinColumn(name = "attribute_values_id", referencedColumnName = "_id", nullable = false, insertable = false, updatable = false)
    public AttributeValues getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(AttributeValues attributeValuesByAttributeValuesId) {
        this.attributeValues = attributeValuesByAttributeValuesId;
    }

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "_id", nullable = false, insertable = false, updatable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
