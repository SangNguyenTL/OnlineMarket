package OnlineMarket.service;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.PostCategory;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.postCategory.PostCategoryHasPostException;
import OnlineMarket.util.exception.postCategory.PostCategoryNotFoundException;

import java.util.List;

public interface PostCategoryService{

    void save(PostCategory postCategory);

    void update(PostCategory entity) throws PostCategoryNotFoundException;

    void delete(Integer key) throws PostCategoryNotFoundException, PostCategoryHasPostException;

    PostCategory getByKey(Integer key);

    PostCategory getByDeclaration(String key, Object value);

    ResultObject<PostCategory> list(FilterForm filterForm);

    List<PostCategory> list();
}
