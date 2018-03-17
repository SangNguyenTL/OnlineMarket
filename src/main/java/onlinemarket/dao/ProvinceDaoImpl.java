package onlinemarket.dao;

import org.springframework.stereotype.Repository;

import onlinemarket.model.Province;

@Repository("provinceDao")
public class ProvinceDaoImpl extends AbstractDao<Integer, Province> implements ProvinceDao{

}
