package OnlineMarket.dao;

import OnlineMarket.model.Comment;
import org.springframework.stereotype.Repository;

@Repository("commentDao")
public class CommentDaoImpl extends AbstractDao<Integer, Comment> implements CommentDao{
}
