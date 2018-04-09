package onlinemarket.dao;

import onlinemarket.model.Post;
import org.springframework.stereotype.Repository;

@Repository("postDao")
public class PostDaoImpl extends AbstractDao<Integer, Post> implements PostDao {
}
