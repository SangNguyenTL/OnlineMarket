package onlinemarket.util;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import onlinemarket.form.config.UploadConfig;
import onlinemarket.form.config.UploadForm;
import onlinemarket.service.config.ConfigurationService;
 
 
@Component
public class FileValidator implements Validator {
     
	@Autowired
	ConfigurationService configurationService;
	
    public boolean supports(Class<?> clazz) {
        return UploadForm.class.isAssignableFrom(clazz);
    }
 
    public void validate(Object obj, Errors errors) {
    	UploadConfig config = configurationService.getUpload();
    	UploadForm uploadForm = (UploadForm) obj;
    	for (MultipartFile file : uploadForm.getFiles()) {
			long fileSize = file.getSize();
			
			if (fileSize == 0) errors.rejectValue(file.getOriginalFilename(), 
					"{file.mising}");
			if(!MimeTypesImage.lookupMimeType(file.getContentType().toLowerCase()))
				errors.rejectValue(file.getContentType(), "{file.not.allow}");
			if(fileSize > config.getMaxFileSize())
				errors.rejectValue(file.getOriginalFilename(), "{file.via.limit.size}");
		}
    }
}