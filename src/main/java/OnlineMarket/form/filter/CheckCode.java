package OnlineMarket.form.filter;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class CheckCode {

    @NotEmpty
    @Size(max = 12)
    private String code;

    @NotEmpty
    private Integer[] productIds;

    public CheckCode() {
    }

    public CheckCode(String code, Integer[] productIds) {
        this.code = code;
        this.productIds = productIds;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer[] getProductIds() {
        return productIds;
    }

    public void setProductIds(Integer[] productIds) {
        this.productIds = productIds;
    }
}
