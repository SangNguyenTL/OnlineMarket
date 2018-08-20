package OnlineMarket.service;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Comment;
import OnlineMarket.model.Post;
import OnlineMarket.model.User;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.post.PostNotFoundException;
import OnlineMarket.util.exception.user.UserNotFoundException;

public interface CommentService {

    void save(Comment comment) throws PostNotFoundException, UserNotFoundException;

    ResultObject<Comment> list(FilterForm filterForm);

    void delete(Integer id) throws CustomException;

    Comment activeComment(int id) throws CustomException;

    ResultObject<Comment> listByUserKey(User user, FilterForm filterForm);

    ResultObject<Comment> listByPost(Post post, FilterForm filterForm);
}
