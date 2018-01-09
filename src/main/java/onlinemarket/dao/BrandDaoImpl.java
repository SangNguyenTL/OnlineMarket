package onlinemarket.dao;

import org.springframework.stereotype.Repository;

import onlinemarket.model.Brand;

@Repository("brandDao")
public class BrandDaoImpl extends AbstractDao<Integer, Brand> implements BrandDao{

}
