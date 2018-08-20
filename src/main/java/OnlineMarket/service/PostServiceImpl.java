package OnlineMarket.service;

import OnlineMarket.dao.CommentDao;
import OnlineMarket.dao.PostCategoryDao;
import OnlineMarket.dao.PostDao;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Post;
import OnlineMarket.model.PostCategory;
import OnlineMarket.model.User;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.Slugify;
import OnlineMarket.util.exception.post.PostNotFoundException;
import OnlineMarket.util.exception.postCategory.PostCategoryNotFoundException;
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
        if(postType.equals("post")){
            PostCategory postCategory = postCategoryDao.getByKey(post.getPostCategory().getId());
            if(post.getPostType().equals("post") && postCategory == null) throw new PostCategoryNotFoundException();
            post.setPostCategory(postCategory);
        }
        post.setSlug(slugify.slugify(post.getSlug()));
        post.setUser(user);
        post.setPostType(postType);
        postDao.save(post);
    }

    @Override
    public void update(Post post, User user, String postType) throws PostCategoryNotFoundException, PostNotFoundException {
        Post postCheck = postDao.getByKey(post.getId());
        if(postCheck == null) throw new PostNotFoundException();

        post.setPostType(postType);
        if(post.getPostType().equals("post")){
            PostCategory postCategory = postCategoryDao.getByKey(post.getPostCategory().getId());
            if( (postCategory == null))
                throw new PostCategoryNotFoundException();
            post.setPostCategory(postCategory);
        }
        post.setCreateDate(postCheck.getCreateDate());
        post.setUpdateDate(new Date());
        post.setUser(user);
        post.setSlug(slugify.slugify(post.getSlug()));
        postDao.merge(post);
    }

    @Override
    public void delete(Integer id) throws PostNotFoundException {
        Post post = postDao.getByKey(id);
        if(post == null) throw new PostNotFoundException();
        postDao.delete(post);
    }

    @Override
    public ResultObject<Post> list(FilterForm filterForm) {
        return postDao.list(filterForm);
    }
}
