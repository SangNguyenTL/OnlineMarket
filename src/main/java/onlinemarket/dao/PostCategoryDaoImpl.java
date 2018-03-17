package onlinemarket.dao;

import org.springframework.stereotype.Repository;

import onlinemarket.model.PostCategory;

@Repository("postCategoryDao")
public class PostCategoryDaoImpl extends AbstractDao<Integer, PostCategory> implements PostCategoryDao{

}
