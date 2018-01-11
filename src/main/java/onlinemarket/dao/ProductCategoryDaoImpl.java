package onlinemarket.dao;

import org.springframework.stereotype.Repository;

import onlinemarket.model.ProductCategory;

@Repository("productCategoryDao")
public class ProductCategoryDaoImpl extends AbstractDao<Integer, ProductCategory> implements ProductCategoryDao{

	
}
