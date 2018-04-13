package onlinemarket.controller.web;

import java.io.IOException;

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

import onlinemarket.form.config.UploadForm;
import onlinemarket.form.filter.ImageFilter;
import onlinemarket.model.Image;
import onlinemarket.service.ImageService;
import onlinemarket.util.exception.CreateFolderException;
import onlinemarket.util.exception.UploadTypeException;
import onlinemarket.util.FileValidator;
import onlinemarket.util.ResponseResult;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/image")
public class ApiImageController {

    @Autowired
    FileValidator fileValidator;

    @Autowired
    ImageService imageService;

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

    @RequestMapping(value = "/load", produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<?> getUser(@ModelAttribute ImageFilter imageFilter) {
        return ResponseEntity.ok().body(imageService.filter(imageFilter));
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> upload(@Valid @ModelAttribute UploadForm uploadForm) {
        try {
            return ResponseEntity.ok().body(imageService.save(uploadForm));
        } catch (IllegalStateException | IOException | UploadTypeException | CreateFolderException e) {
            return ResponseEntity.badRequest().body(new ResponseResult(true, e.getMessage()));
        }
    }
}
