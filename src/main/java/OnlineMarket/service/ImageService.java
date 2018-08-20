package OnlineMarket.service;

import java.io.IOException;
import java.util.List;

import OnlineMarket.form.config.UploadForm;
import OnlineMarket.form.filter.ImageFilter;
import OnlineMarket.model.Image;
import OnlineMarket.result.api.ResultImage;
import OnlineMarket.util.exception.CreateFolderException;
import OnlineMarket.util.exception.UploadTypeException;

public interface ImageService extends InterfaceService<Integer, Image>{
	
	List<Image> save(UploadForm form) throws IllegalStateException, IOException, UploadTypeException, CreateFolderException;
	
	ResultImage filter(ImageFilter imageFilter);

	void remove(Image image);
}
