package onlinemarket.form.filter;

import onlinemarket.validation.StringContain;

public class FilterProduct {

    @StringContain(acceptedValues = {"name-a-z", "name-z-a", "price-low-high", "price-high-low", "rating-lowest", "rating-highest", "model-a-z", "model-z-a"})
    private String orderBy;

    private FilterForm filterForm;

    private String state;

    public FilterForm getFilterForm() {
        return filterForm;
    }

    public void setFilterForm(FilterForm filterForm) {
        this.filterForm = filterForm;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public FilterProduct(FilterForm filterForm) {
        this.filterForm = filterForm;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
