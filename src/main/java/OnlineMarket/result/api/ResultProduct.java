package OnlineMarket.result.api;

import OnlineMarket.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ResultProduct {

    private List<Product> results = new ArrayList<>();

    private Pagination pagination = new Pagination(false);

    public List<Product> getResults() {
        return results;
    }

    public void setResults(List<Product> results) {
        this.results = results;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}

