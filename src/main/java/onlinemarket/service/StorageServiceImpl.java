package onlinemarket.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import onlinemarket.form.config.UploadForm;
import onlinemarket.service.exception.CreateFolderException;
import onlinemarket.service.exception.UploadTypeException;

@Service
public class StorageServiceImpl implements StorageService{

	final String rootPath = "/assets/images/";
	
	@Autowired
	ServletContext context;

	@Override
	public List<File> store(UploadForm form) throws IllegalStateException, IOException, UploadTypeException, CreateFolderException {
		List<MultipartFile> files = form.getFiles();
		String uploadType = form.getUploadType();
		Date cDate = new Date();
		String cYearMonth = new SimpleDateFormat("YYYY/MM").format(cDate);
		long cTime = cDate.getTime();
		String[] allowType = {"site", "product", "user", "event", "post"};
		if(!ArrayUtils.contains(allowType, uploadType)) throw new UploadTypeException("Upload Type is isvalid");
		String saveFolder = uploadType;
		String drirectory = rootPath+saveFolder+"/"+cYearMonth;
		if(!createFolder(drirectory)) throw new CreateFolderException("Can not create directory.");
		List<File> fileList = new ArrayList<File>();
		for (MultipartFile multipartFile : files) {
			String originalFilename = multipartFile.getOriginalFilename(),
					newName = String.valueOf(cTime)+ "-" + originalFilename;
			File destinationFile = new File(context.getRealPath(drirectory)+"/"+newName);
			multipartFile.transferTo(destinationFile);
			fileList.add(destinationFile);
		}
		return fileList;
	}

	@Override
	public void delete(String path) {
		File file = new File(context.getRealPath(path));
		if(file.exists()) file.delete();
	}
	
	public boolean createFolder(String path) {
		File folder = new File(context.getRealPath(path));
		boolean flag = true;
		if(!folder.exists())
			 flag = new File(context.getRealPath(path)).mkdirs();
		return flag;
	}
}
