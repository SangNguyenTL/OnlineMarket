package onlinemarket.form.config;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import onlinemarket.validation.StringContain;

public class UploadForm {
	
	private List<MultipartFile> files;
	
	@NotEmpty
	@StringContain(acceptedValues= {"site", "post", "product", "brand", "user"})
	private
	String uploadType;

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}
	
}
