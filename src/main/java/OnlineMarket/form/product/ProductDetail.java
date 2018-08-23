package OnlineMarket.form.product;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class ProductDetail {
    @NotNull
    private Integer id;

    @NotNull
    @Range(min = 1, max = 100)
    private Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
