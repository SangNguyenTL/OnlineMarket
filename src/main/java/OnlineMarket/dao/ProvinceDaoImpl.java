package OnlineMarket.dao;

import org.springframework.stereotype.Repository;

import OnlineMarket.model.Province;

@Repository("provinceDao")
public class ProvinceDaoImpl extends AbstractDao<Integer, Province> implements ProvinceDao{

}
