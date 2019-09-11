package OnlineMarket.dao;

import org.springframework.stereotype.Repository;

import OnlineMarket.model.Post;

@Repository("postDao")
public class PostDaoImpl extends AbstractDao<Integer, Post> implements PostDao {

}
