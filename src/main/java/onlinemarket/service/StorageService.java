package onlinemarket.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import onlinemarket.form.config.UploadForm;
import onlinemarket.util.exception.CreateFolderException;
import onlinemarket.util.exception.UploadTypeException;

public interface StorageService {
	
	List<File> store(UploadForm form) throws IllegalStateException, IOException, UploadTypeException, CreateFolderException;
	
	boolean delete(String fileName);
	
}
