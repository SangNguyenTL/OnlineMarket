package onlinemarket.dao;

import org.springframework.stereotype.Repository;

import onlinemarket.model.PostCategory;

@Repository("postCategoryDao")
public abstract class PostCategoryDaoImpl extends AbstractDao<Integer, PostCategory> implements PostCategoryDao{

}
