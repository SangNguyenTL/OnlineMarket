package onlinemarket.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tb_attribute_values", schema = "dbo", catalog = "SmartMarket")
public class AttributeValues {
    private Integer id;
    private String value;
    private String link;
    private Attribute attribute;
    private Collection<ProductAttributeValues> productAttributeValues;

    public AttributeValues() {
    }

    public AttributeValues(String value, Attribute attribute) {
        this.value = value;
        this.attribute = attribute;
    }

    public AttributeValues(String value, String link, Attribute attribute) {
        this.value = value;
        this.link = link;
        this.attribute = attribute;
    }

    public AttributeValues(Integer id) {
    }


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "_id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @NotEmpty
    @Size(max = 500)
    @Column(name = "value", nullable = false, length = 500)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Basic
    @Size(max = 2088)
    @Column(name = "link", nullable = true, length = 2083)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeValues that = (AttributeValues) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(value, that.value) &&
                Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, value, link);
    }

    @ManyToOne
    @JoinColumn(name = "attribute_id", referencedColumnName = "_id", nullable = false)
    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    @OneToMany(mappedBy = "attributeValues")
    public Collection<ProductAttributeValues> getProductAttributeValues() {
        return productAttributeValues;
    }

    public void setProductAttributeValues(Collection<ProductAttributeValues> productAttributeValuesById) {
        this.productAttributeValues = productAttributeValuesById;
    }
}
