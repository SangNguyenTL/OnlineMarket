package onlinemarket.controller.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import onlinemarket.form.config.UploadForm;
import onlinemarket.form.filter.ImageFilter;
import onlinemarket.model.Image;
import onlinemarket.model.User;
import onlinemarket.service.ImageService;
import onlinemarket.service.UserService;
import onlinemarket.service.exception.CreateFolderException;
import onlinemarket.service.exception.UploadTypeException;
import onlinemarket.util.FileValidator;
import onlinemarket.util.ResponseResult;

@RestController
@RequestMapping("/api/image")
public class ApiImageController {

	@Autowired
	private UserService userService;

	private User currentUser;

	@Autowired
	FileValidator fileValidator;

	@Autowired
	ImageService imageService;

	@ModelAttribute("currentUser")
	public User getCurrentUser() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}

		currentUser = userService.getByEmail(userName);
		if (currentUser == null) {
			currentUser = new User();
			currentUser.setEmail(userName);
		}
		return currentUser;
	}

	@InitBinder("uploadFrom")
	protected void initBinderUploadFrom(WebDataBinder binder) {
		binder.setValidator(fileValidator);
	}

	@RequestMapping(value = "/delete/{id:^\\d+}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUser(@PathVariable("id") Integer id) {
		Image image = imageService.getByKey(id);
		if (image == null)
			return ResponseEntity.ok(new ResponseResult(true, "Image not found!"));
		imageService.remove(image);
		return ResponseEntity.ok().body(new ResponseResult(false, "Image was removed!"));
	}

	@RequestMapping(value = "/load",
			produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.POST,
			RequestMethod.GET })
	public ResponseEntity<?> getUser(@ModelAttribute("imageFilter") ImageFilter imageFilter) throws Exception {
		return ResponseEntity.ok().body(imageService.filter(imageFilter));
	}

	@RequestMapping(value = "/upload",
			method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> upload(@ModelAttribute("uploadFrom") UploadForm uploadForm, BindingResult result) {

		List<String> listError = new ArrayList<>();
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				listError.add(error.getDefaultMessage());
			}
			return ResponseEntity.badRequest().body(listError);
		}
		try {
			return ResponseEntity.ok().body(imageService.save(uploadForm, currentUser));
		} catch (IllegalStateException | IOException | UploadTypeException | CreateFolderException e) {
			return ResponseEntity.badRequest().body(new ResponseResult(true, e.getMessage()));
		}
	}
}
