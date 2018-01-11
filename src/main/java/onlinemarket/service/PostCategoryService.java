package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.PostCategory;
import onlinemarket.result.ResultObject;

public interface PostCategoryService extends InterfaceService<Integer, PostCategory>{
	
	public ResultObject<PostCategory> pagination(Integer currentPage, Integer size);
	
	public ResultObject<PostCategory> list(FilterForm filterForm);
}
