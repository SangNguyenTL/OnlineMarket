package onlinemarket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.ProvinceDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Province;
import onlinemarket.result.ResultObject;

@Service("provinceService")
@Transactional
public class ProvinceServiceImpl implements ProvinceService{

	@Autowired
	ProvinceDao provinceDao;
	
	@Override
	public List<Province> list() {
		return provinceDao.list();
	}

	@Override
	public void save(Province entity) {
		
	}

	@Override
	public void update(Province entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Province entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Province getByKey(Byte key) {
		return provinceDao.getByKey(key);
	}

	@Override
	public List<Province> list(Integer offset, Integer maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Province getByDeclaration(String key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultObject<Province> list(FilterForm filterForm) {
		return provinceDao.list(filterForm);
	}

}
