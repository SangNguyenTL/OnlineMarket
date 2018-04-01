package onlinemarket.service;

import java.util.List;

import onlinemarket.util.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.PostCategoryDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.PostCategory;
import onlinemarket.result.ResultObject;

@Service("postCategoryService")
@Transactional
public class PostCategoryServiceImpl implements PostCategoryService{

	@Autowired
	PostCategoryDao postCategoryDao;

	@Override
	public void save(PostCategory entity) {
		postCategoryDao.save(entity);
		
	}

	@Override
	public void update(PostCategory entity) throws CustomException {
		postCategoryDao.update(entity);
		
	}

	@Override
	public void delete(PostCategory entity) {
		postCategoryDao.delete(entity);
		
	}

	@Override
	public PostCategory getByKey(Integer key) {
		return postCategoryDao.getByKey(key);
	}

	@Override
	public PostCategory getByDeclaration(String key, String value) {
		return postCategoryDao.getByDeclaration(key, value);
	}

	@Override
	public List<PostCategory> list() {
		return postCategoryDao.list();
	}

	@Override
	public List<PostCategory> list(Integer offset, Integer maxResults) {
		return postCategoryDao.list(offset, maxResults);
	}

	@Override
	public ResultObject<PostCategory> pagination(Integer currentPage, Integer size) {
		if(size < 1) size = 1;
		if(size > 50) size = 50;
		if(currentPage < 1) currentPage = 1;
		
		ResultObject<PostCategory> result = new ResultObject<>();
		
		Integer totalPages = (int) Math.ceil(postCategoryDao.count() / size);
		
		result.setTotalPages(totalPages);
		
		result.setCurrentPage(currentPage);
		
		result.setList(postCategoryDao.list((currentPage - 1) * size, size));
		
		return result;
	}

	@Override
	public ResultObject<PostCategory> list(FilterForm filterForm) {
		return postCategoryDao.list(filterForm);
	}
}
