package onlinemarket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.BrandDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.result.ResultObject;

@Service("brandService")
@Transactional
public class BrandServiceImpl implements BrandService{

	@Autowired
	BrandDao brandDao;
	
	@Override
	public void save(Brand entity) {
		brandDao.save(entity);
	}

	@Override
	public void update(Brand entity) {
		brandDao.update(entity);
	}

	@Override
	public void delete(Brand entity) {
		brandDao.delete(entity);
	}

	@Override
	public Brand getByKey(Integer key) {
		return brandDao.getByKey(key);
	}

	@Override
	@Transactional(readOnly=true)
	public Brand getByDeclaration(String key, String value) {
		return brandDao.getByDeclaration(key, value);
	}

	@Override
	public List<Brand> list() {
		return brandDao.list();
	}

	@Override
	public List<Brand> list(Integer offset, Integer maxResults) {
		return brandDao.list(offset, maxResults);
	}

	@Override
	public ResultObject<Brand> pagination(Integer currentPage, Integer size) {
		
		if(size < 1) size = 1;
		if(size > 50) size = 50;
		if(currentPage < 1) currentPage = 1;
		
		ResultObject<Brand> result = new ResultObject<>();
		
		Integer totalPages = (int) Math.ceil(brandDao.count() / size);
		
		result.setTotalPages(totalPages);
		
		result.setCurrentPage(currentPage);
		
		result.setList(brandDao.list((currentPage - 1) * size, size));
		
		return result;
	}

	@Override
	public ResultObject<Brand> list(FilterForm filterForm) {
		return brandDao.list(filterForm);
	}

}
