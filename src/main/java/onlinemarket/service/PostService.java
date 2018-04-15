package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Post;
import onlinemarket.model.User;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.post.PostHasCommentException;
import onlinemarket.util.exception.post.PostNotFoundException;
import onlinemarket.util.exception.postCategory.PostCategoryNotFoundException;

public interface PostService {

    Post getByKey(Integer key);

    Post getByDeclaration(String key, Object value);

    void save(Post post, User user, String postType) throws PostCategoryNotFoundException;

    void update(Post post, User user, String postType) throws PostCategoryNotFoundException, PostNotFoundException;

    void delete(Integer id) throws PostNotFoundException;

    ResultObject<Post> list(FilterForm filterForm);

}
