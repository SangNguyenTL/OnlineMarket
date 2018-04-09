package onlinemarket.service;

import java.util.Date;
import java.util.List;


import onlinemarket.dao.PostDao;
import onlinemarket.util.Slugify;
import onlinemarket.util.exception.postCategory.PostCategoryHasPostException;
import onlinemarket.util.exception.postCategory.PostCategoryNotFoundException;
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

	@Autowired
	PostDao postDao;

	@Autowired
	Slugify slugify;

	@Override
	public void save(PostCategory entity) {
		entity.setCreateDate(new Date());
		entity.setSlug(slugify.slugify(entity.getSlug()));
		postCategoryDao.save(entity);
	}

	@Override
	public void update(PostCategory entity) throws PostCategoryNotFoundException {
		PostCategory postCategory = postCategoryDao.getByKey(entity.getId());
		if(postCategory == null) throw new PostCategoryNotFoundException();
		entity.setUpdateDate(new Date());
		entity.setSlug(slugify.slugify(entity.getSlug()));
		postCategoryDao.update(entity);
	}

	@Override
	public void delete(Integer key) throws PostCategoryNotFoundException, PostCategoryHasPostException {
		PostCategory postCategory = postCategoryDao.getByKey(key);
		if(postCategory == null) throw new PostCategoryNotFoundException();
		if(postDao.getUniqueResultBy("postCategory", postCategory) != null) throw new PostCategoryHasPostException();
		postCategoryDao.delete(postCategory);
	}

	@Override
	public PostCategory getByKey(Integer key) {
		return postCategoryDao.getByKey(key);
	}

	@Override
	public PostCategory getByDeclaration(String key, Object value) {
		return postCategoryDao.getByDeclaration(key, value);
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

	@Override
	public List<PostCategory> list() {
		return postCategoryDao.list();
	}
}
