package OnlineMarket.util;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import OnlineMarket.form.config.UploadConfig;
import OnlineMarket.form.config.UploadForm;
import OnlineMarket.service.config.ConfigurationService;
 
 
@Component
public class FileValidator implements Validator {
     
	@Autowired
	ConfigurationService configurationService;

	@Override
    public boolean supports(Class<?> clazz) {
        return UploadForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
    	UploadConfig config = configurationService.getUpload();
    	UploadForm uploadForm = (UploadForm) obj;
    	long maxSize  = 0;
    	for (int i= 0; i < uploadForm.getFiles().size(); i++) {
            MultipartFile file = uploadForm.getFiles().get(i);
			long fileSize = file.getSize();
			maxSize = maxSize + fileSize;
			if (fileSize == 0) errors.rejectValue("files["+i+"]","FileMissing", "The file Missing");
			if(!MimeTypesImage.lookupMimeType(file.getContentType().toLowerCase()))
				errors.rejectValue("files["+i+"]", "FileTypeNotAllow", "The file type not allow");
			if(fileSize > config.getMaxFileSize() * 1024 * 1024)
				errors.rejectValue("files["+i+"]", "LimitFileSize", "The file is to large");
		}
		if(maxSize > config.getMaxSize() * 1024 * 1024) errors.rejectValue("files", "LimitSize", "The files is to large");
    }
}