package onlinemarket.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import onlinemarket.util.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.ImageDao;
import onlinemarket.form.config.UploadForm;
import onlinemarket.form.filter.ImageFilter;
import onlinemarket.model.Image;
import onlinemarket.model.User;
import onlinemarket.result.api.ResultImage;
import onlinemarket.util.exception.CreateFolderException;
import onlinemarket.util.exception.UploadTypeException;

@Service("imageService")
@Transactional
public class ImageServiceImpl implements ImageService {

	@Autowired
	ServletContext context;

	@Autowired
	ImageDao imageDao;

	@Autowired
	StorageService storageService;

	@Override
	public void save(Image entity) {
		imageDao.save(entity);
	}

	@Override
	public void update(Image entity) throws CustomException {
		imageDao.update(entity);
	}

	@Override
	public void delete(Image entity) {
		imageDao.delete(entity);
		storageService.delete(entity.getPath());
	}

	@Override
	public Image getByKey(Integer key) {
		return imageDao.getByKey(key);
	}

	@Override
	public Image getByDeclaration(String key, String value) {
		return null;
	}

	@Override
	public List<Image> list() {
		return imageDao.list();
	}

	@Override
	public List<Image> list(Integer offset, Integer maxResults) {
		return imageDao.list(offset, maxResults);
	}

	@Override
	public List<Image> save(UploadForm form, User user)
			throws IllegalStateException, IOException, UploadTypeException, CreateFolderException {
		List<File> fileList = storageService.store(form);
		File basePath = new File(context.getRealPath(""));
		List<Image> imageList = new ArrayList<>();
		for (File file : fileList) {
			Image image = new Image();
			String name = file.getName().substring(file.getName().indexOf("-") + 1);
			image.setName(name);
			String path = "/" + basePath.toURI().relativize(file.toURI()).getPath();
			image.setPath(path);
			image.setDataType(form.getUploadType());
			image.setUploadDate(new Date());
			image.setUser(user);
			imageDao.save(image);
			imageList.add(image);
		}
		return imageList;
	}

	@Override
	public ResultImage filter(ImageFilter imageFilter) {
		return imageDao.filter(imageFilter);
	}

	@Override
	public void remove(Image image) {
		imageDao.delete(image);
		storageService.delete(image.getPath());
	}

}
