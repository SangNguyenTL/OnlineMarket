package onlinemarket.service;

import java.util.List;

import onlinemarket.util.exception.CustomException;
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
		provinceDao.save(entity);
	}

	@Override
	public void update(Province entity) throws CustomException {
		Province provinceCheck = provinceDao.getByKey(entity.getId());
		if (provinceCheck == null)
			throw new CustomException("Province not found");
		provinceDao.update(entity);
	}

	@Override
	public void delete(Province entity) {
		provinceDao.delete(entity);
	}

	@Override
	public Province getByKey(Integer key) {
		return provinceDao.getByKey(key);
	}

	@Override
	public List<Province> list(Integer offset, Integer maxResults) {
		return provinceDao.list(offset, maxResults);
	}

	@Override
	public Province getByDeclaration(String key, String value) {
		return provinceDao.getByDeclaration(key, value);
	}

	@Override
	public ResultObject<Province> list(FilterForm filterForm) {
		return provinceDao.list(filterForm);
	}

}
