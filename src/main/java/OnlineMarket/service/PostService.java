package OnlineMarket.service;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Post;
import OnlineMarket.model.User;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.post.PostNotFoundException;
import OnlineMarket.util.exception.postCategory.PostCategoryNotFoundException;

public interface PostService {

    Post getByKey(Integer key);

    Post getByDeclaration(String key, Object value);

    void save(Post post, User user, String postType) throws PostCategoryNotFoundException;

    void update(Post post, User user, String postType) throws PostCategoryNotFoundException, PostNotFoundException;

    void delete(Integer id) throws PostNotFoundException;

    ResultObject<Post> list(FilterForm filterForm);

}
