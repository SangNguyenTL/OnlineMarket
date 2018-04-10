package onlinemarket.service;

import onlinemarket.dao.CommentDao;
import onlinemarket.dao.PostCategoryDao;
import onlinemarket.dao.PostDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Post;
import onlinemarket.model.PostCategory;
import onlinemarket.model.User;
import onlinemarket.result.ResultObject;
import onlinemarket.util.Slugify;
import onlinemarket.util.exception.post.PostHasCommentException;
import onlinemarket.util.exception.post.PostNotFoundException;
import onlinemarket.util.exception.postCategory.PostCategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service("postService")
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    PostDao postDao;

    @Autowired
    PostCategoryDao postCategoryDao;

    @Autowired
    CommentDao commentDao;

    @Autowired
    Slugify slugify;

    @Override
    public Post getByKey(Integer key) {
        return postDao.getByKey(key);
    }

    @Override
    public Post getByDeclaration(String key, Object value) {
        return postDao.getByDeclaration(key, value);
    }

    @Override
    public void save(Post post, User user, String postType) throws PostCategoryNotFoundException{
        post.setCreateDate(new Date());
        PostCategory postCategory = postCategoryDao.getByKey(post.getPostCategory().getId());
        post.setPostType(postType);
        if(post.getPostType().equals("post") && postCategory == null) throw new PostCategoryNotFoundException();
        post.setSlug(slugify.slugify(post.getSlug()));
        post.setPostType(postType);
        postDao.save(post);
    }

    @Override
    public void update(Post post, User user, String postType) throws PostCategoryNotFoundException, PostNotFoundException {
        if(postDao.getByKey(post.getId()) == null) throw new PostNotFoundException();
        post.setPostType(postType);
        if(post.getPostType().equals("post") && (post.getPostCategory() == null || postDao.getByKey(post.getPostCategory().getId()) == null)) throw new PostCategoryNotFoundException();
        post.setUpdateDate(new Date());
        postDao.update(post);
    }

    @Override
    public void delete(Integer id) throws PostNotFoundException, PostHasCommentException {
        Post post = postDao.getByKey(id);
        if(post == null) throw new PostNotFoundException();
        if(commentDao.getUniqueResultBy("post", post) != null) throw new PostHasCommentException();
        postDao.delete(post);
    }

    @Override
    public ResultObject<Post> list(FilterForm filterForm) {
        return postDao.list(filterForm);
    }
}
