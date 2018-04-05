package onlinemarket.form.filter;

public class FilterProduct {

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
}
