package onlinemarket.service;

import java.io.IOException;
import java.util.List;

import onlinemarket.form.config.UploadForm;
import onlinemarket.form.filter.ImageFilter;
import onlinemarket.model.Image;
import onlinemarket.model.User;
import onlinemarket.result.api.ResultImage;
import onlinemarket.util.exception.CreateFolderException;
import onlinemarket.util.exception.UploadTypeException;

public interface ImageService extends InterfaceService<Integer, Image>{
	
	List<Image> save(UploadForm form, User user) throws IllegalStateException, IOException, UploadTypeException, CreateFolderException;
	
	ResultImage filter(ImageFilter imageFilter);

	void remove(Image image);
}
