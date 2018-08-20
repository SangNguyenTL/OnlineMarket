package OnlineMarket.dao;

import org.springframework.stereotype.Repository;

import OnlineMarket.model.PostCategory;


@Repository("postCategoryDao")
public class PostCategoryDaoImpl extends AbstractDao<Integer, PostCategory> implements PostCategoryDao{
}
