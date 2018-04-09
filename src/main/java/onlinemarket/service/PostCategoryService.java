package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.PostCategory;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.postCategory.PostCategoryHasPostException;
import onlinemarket.util.exception.postCategory.PostCategoryNotFoundException;

import java.util.List;

public interface PostCategoryService{

    void save(PostCategory postCategory);

    void update(PostCategory entity) throws PostCategoryNotFoundException;

    void delete(Integer key) throws PostCategoryNotFoundException, PostCategoryHasPostException;

    PostCategory getByKey(Integer key);

    PostCategory getByDeclaration(String key, Object value);

    ResultObject<PostCategory> pagination(Integer currentPage, Integer size);

    ResultObject<PostCategory> list(FilterForm filterForm);

    List<PostCategory> list();
}
