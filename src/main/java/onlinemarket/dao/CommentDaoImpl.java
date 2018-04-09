package onlinemarket.dao;

import onlinemarket.model.Comment;
import org.springframework.stereotype.Repository;

@Repository("commentDao")
public class CommentDaoImpl extends AbstractDao<Integer, Comment> implements CommentDao{
}
