package OnlineMarket.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import OnlineMarket.form.config.UploadForm;
import OnlineMarket.util.exception.CreateFolderException;
import OnlineMarket.util.exception.UploadTypeException;

public interface StorageService {
	
	List<File> store(UploadForm form) throws IllegalStateException, IOException, UploadTypeException, CreateFolderException;
	
	boolean delete(String fileName);
	
}
