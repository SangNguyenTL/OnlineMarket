package OnlineMarket.service;

import java.util.Date;
import java.util.List;


import OnlineMarket.dao.PostDao;
import OnlineMarket.util.Slugify;
import OnlineMarket.util.exception.postCategory.PostCategoryHasPostException;
import OnlineMarket.util.exception.postCategory.PostCategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import OnlineMarket.dao.PostCategoryDao;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.PostCategory;
import OnlineMarket.result.ResultObject;

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
        entity.setCreateDate(entity.getCreateDate());
        entity.setSlug(slugify.slugify(entity.getSlug()));
		postCategoryDao.merge(entity);
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
	public ResultObject<PostCategory> list(FilterForm filterForm) {
		return postCategoryDao.list(filterForm);
	}

	@Override
	public List<PostCategory> list() {
		return postCategoryDao.list();
	}
}
