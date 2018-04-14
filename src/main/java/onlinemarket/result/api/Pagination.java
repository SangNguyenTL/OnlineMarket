package onlinemarket.result.api;

public class Pagination {

    public Pagination(boolean more) {
        this.more = more;
    }

    private boolean more;

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }
}
