package onlinemarket.service;

import java.io.IOException;
import java.util.List;

import onlinemarket.form.config.UploadForm;
import onlinemarket.model.Image;
import onlinemarket.model.User;
import onlinemarket.service.exception.CreateFolderException;
import onlinemarket.service.exception.UploadTypeException;

public interface ImageService extends InterfaceService<Integer, Image>{
	
	List<Image> save(UploadForm form, User user) throws IllegalStateException, IOException, UploadTypeException, CreateFolderException;
	
}
