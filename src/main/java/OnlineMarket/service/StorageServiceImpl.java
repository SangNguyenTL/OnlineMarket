package OnlineMarket.service;

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

import OnlineMarket.form.config.UploadForm;
import OnlineMarket.util.exception.CreateFolderException;
import OnlineMarket.util.exception.UploadTypeException;

@Service
public class StorageServiceImpl implements StorageService{


	@Autowired
	ServletContext context;

	@Override
	public List<File> store(UploadForm form) throws IllegalStateException, IOException, UploadTypeException, CreateFolderException {
		String rootPath = "/assets/images/";
		List<MultipartFile> files = form.getFiles();
		String uploadType = form.getUploadType();
		Date cDate = new Date();
		String cYearMonth = new SimpleDateFormat("YYYY/MM").format(cDate);
		long cTime = cDate.getTime();
		String[] allowType = {"site", "product", "user", "event", "post", "brand"};
		if(!ArrayUtils.contains(allowType, uploadType)) throw new UploadTypeException("Upload Type is isvalid");
		String directory = rootPath+uploadType+"/"+cYearMonth;
		if(!createFolder(directory)) throw new CreateFolderException("Can not create directory.");
		List<File> fileList = new ArrayList<>();
		for (MultipartFile multipartFile : files) {
			String originalFilename = multipartFile.getOriginalFilename(),
					newName = String.valueOf(cTime)+ "-" + originalFilename;
			File destinationFile = new File(context.getRealPath(directory)+"/"+newName);
			multipartFile.transferTo(destinationFile);
			fileList.add(destinationFile);
		}
		return fileList;
	}

	@Override
	public boolean delete(String path) {
		if(path == null) return true;
		path = context.getRealPath(path);

		File file = new File(path);
		if(file.exists()) return file.delete();
		else return false;
	}
	
	private boolean createFolder(String path) {
		if(path == null) return false;
		path = context.getRealPath(path);
		File folder = new File(path);
		boolean flag = true;
		if(!folder.exists())
			 flag = new File(path).mkdirs();
		return flag;
	}
}
