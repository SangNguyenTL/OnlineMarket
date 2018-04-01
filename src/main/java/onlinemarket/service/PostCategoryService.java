package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.PostCategory;
import onlinemarket.result.ResultObject;

public interface PostCategoryService extends InterfaceService<Integer, PostCategory> {

    ResultObject<PostCategory> pagination(Integer currentPage, Integer size);

    ResultObject<PostCategory> list(FilterForm filterForm);
}
