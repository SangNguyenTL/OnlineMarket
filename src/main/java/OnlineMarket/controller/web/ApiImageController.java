package OnlineMarket.controller.web;

import java.io.IOException;

import OnlineMarket.model.User;
import OnlineMarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import OnlineMarket.form.config.UploadForm;
import OnlineMarket.form.filter.ImageFilter;
import OnlineMarket.model.Image;
import OnlineMarket.service.ImageService;
import OnlineMarket.util.exception.CreateFolderException;
import OnlineMarket.util.exception.UploadTypeException;
import OnlineMarket.util.FileValidator;
import OnlineMarket.util.ResponseResult;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/image")
public class ApiImageController {

    @Autowired
    FileValidator fileValidator;

    @Autowired
    ImageService imageService;

    @Autowired
    UserService userService;

    @InitBinder("uploadFrom")
    protected void initBinderUploadFrom(WebDataBinder binder) {
        binder.setValidator(fileValidator);
    }

    @RequestMapping(value = "/delete/{id:^\\d+}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable("id") Integer id) {
        Image image = imageService.getByKey(id);
        User user = userService.getCurrentUser();
        if(user == null ) return ResponseEntity.ok(new ResponseResult(true, "User not found!"));
        if(user.getRole().getName().equals("USER")){
            if(!image.getUser().getId().equals(user.getId())) return ResponseEntity.ok(new ResponseResult(true, "Access is denied!"));
        }
        if (image == null)
            return ResponseEntity.ok(new ResponseResult(true, "Image not found!"));
        imageService.remove(image);
        return ResponseEntity.ok().body(new ResponseResult(false, "Image was removed!"));
    }

    @RequestMapping(value = "/load", produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<?> getUser(@ModelAttribute ImageFilter imageFilter) {
        User user = userService.getCurrentUser();
        if(user == null )return ResponseEntity.ok().body(null);
        if(user.getRole().getName().equals("USER")){
            imageFilter.setUser(user);
        }
        return ResponseEntity.ok().body(imageService.filter(imageFilter));
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> upload(@Valid @ModelAttribute UploadForm uploadForm) {
        try {
            User user = userService.getCurrentUser();
            if(user == null) throw new UploadTypeException("User not found");
            if(user.getRole().getName().equals("USER")){
                uploadForm.setUploadType("user");
            }
            return ResponseEntity.ok().body(imageService.save(uploadForm));
        } catch (IllegalStateException | IOException | UploadTypeException | CreateFolderException e) {
            return ResponseEntity.badRequest().body(new ResponseResult(true, e.getMessage()));
        }
    }
}
