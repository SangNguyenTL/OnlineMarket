package onlinemarket.dao;

import org.springframework.stereotype.Repository;

import onlinemarket.model.Province;

@Repository("provinceDao")
public class ProvinceDaoImpl extends AbstractDao<Byte, Province> implements ProvinceDao{

}
