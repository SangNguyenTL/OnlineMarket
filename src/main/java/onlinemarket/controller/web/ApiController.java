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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import onlinemarket.form.config.UploadForm;
import onlinemarket.model.User;
import onlinemarket.service.ImageService;
import onlinemarket.service.UserService;
import onlinemarket.service.exception.CreateFolderException;
import onlinemarket.service.exception.UploadTypeException;
import onlinemarket.util.FileValidator;

@RestController
@RequestMapping("/api")
public class ApiController {

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
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        
        currentUser = userService.getByEmail(userName);
        if(currentUser == null) {
            currentUser = new User();
            currentUser.setEmail(userName);
        } 
        return currentUser;
	}
	
	@InitBinder("uploadFrom")
	protected void initBinderUploadFrom(WebDataBinder binder) {
		binder.setValidator(fileValidator);
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUser(){
		return ResponseEntity.ok(userService.getByKey(1));
	}
	
	@RequestMapping(value = "/uploadImage",  method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> upload(@ModelAttribute("uploadFrom") UploadForm uploadForm, BindingResult result){

		List<String> listError = new ArrayList<>();
		if(result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				listError.add(error.getDefaultMessage());
			}
			return ResponseEntity.badRequest().body(listError);
		}
		try {
			return ResponseEntity.ok().body(imageService.save(uploadForm, currentUser));
		}catch (IllegalStateException | IOException | UploadTypeException | CreateFolderException e) {
			listError.add(e.getMessage());
			return ResponseEntity.badRequest().body(listError);
		}
	}
}
