package OnlineMarket.service;

import OnlineMarket.dao.CommentDao;
import OnlineMarket.dao.PostDao;
import OnlineMarket.dao.UserDao;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Comment;
import OnlineMarket.model.Post;
import OnlineMarket.model.User;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.post.PostNotFoundException;
import OnlineMarket.util.exception.user.UserNotFoundException;
import OnlineMarket.util.other.CommentState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service("commentService")
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao commentDao;

    @Autowired
    PostDao postDao;

    @Autowired
    UserDao userDao;

    @Override
    public void save(Comment comment) throws PostNotFoundException, UserNotFoundException {
        if(comment.getPost() == null || comment.getPost().getId() == null || postDao.getByKey(comment.getPost().getId()) == null) throw new PostNotFoundException();
        if(comment.getUser() == null ||  comment.getUser().getId() == null ||userDao.getByKey(comment.getUser().getId()) == null) throw new UserNotFoundException();
        comment.setCreateDate(new Date());
        comment.setStatus(CommentState.INACTIVE.getId());
        commentDao.save(comment);
    }

    @Override
    public ResultObject<Comment> list(FilterForm filterForm) {
        return commentDao.list(filterForm);
    }

    @Override
    public void delete(Integer id) throws CustomException {
        Comment comment = commentDao.getByKey(id);
        if(comment == null) throw new CustomException("The comment not found.");
        commentDao.delete(comment);
    }

    @Override
    public Comment activeComment(int id) throws CustomException {
        Comment comment = commentDao.getByKey(id);
        if(comment == null) throw new CustomException("The comment not found.");
        comment.setStatus(CommentState.ACTIVE.getId());
        comment.setUpdateDate(new Date());
        commentDao.merge(comment);
        return comment;
    }

    @Override
    public ResultObject<Comment> listByUserKey(User user, FilterForm filterForm) {
        return commentDao.listByDeclaration("user", user, filterForm);
    }

    @Override
    public ResultObject<Comment> listByPost(Post post, FilterForm filterForm) {
        return commentDao.listByDeclaration("post", post, filterForm);
    }
}
