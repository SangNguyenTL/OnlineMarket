package onlinemarket.form.filter;

import onlinemarket.model.PostCategory;

public class FilterPost {
    private FilterForm filterForm;
    private String status;
    private PostCategory postCategory;

    public FilterPost(FilterForm filterForm) {
        this.filterForm = filterForm;
    }

    public FilterForm getFilterForm() {
        return filterForm;
    }

    public void setFilterForm(FilterForm filterForm) {
        this.filterForm = filterForm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PostCategory getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(PostCategory postCategory) {
        this.postCategory = postCategory;
    }
}
