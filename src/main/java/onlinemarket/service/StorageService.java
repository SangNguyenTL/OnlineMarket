package onlinemarket.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import onlinemarket.form.config.UploadForm;
import onlinemarket.service.exception.CreateFolderException;
import onlinemarket.service.exception.UploadTypeException;

public interface StorageService {
	
	public List<File> store(UploadForm form) throws IllegalStateException, IOException, UploadTypeException, CreateFolderException;
	
	public void delete(String fileName);
	
}
