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

    void save(Post post, User user) throws PostCategoryNotFoundException;

    void update(Post post, User user) throws PostCategoryNotFoundException, PostNotFoundException;

    void delete(Integer id) throws PostNotFoundException, PostHasCommentException;

    ResultObject<Post> list(FilterForm filterForm);

}
