package OnlineMarket.controller.web;

import OnlineMarket.model.Post;
import OnlineMarket.service.PostService;
import OnlineMarket.result.api.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/post")
@RestController
public class ApiPostController {

    @Autowired
    PostService postService;

    @RequestMapping(value = "/check-slug", produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.POST,
            RequestMethod.GET })
    public ResponseEntity<?> checkSlugUnique(@RequestParam(value = "value") String value,
                                             @RequestParam(value = "id", required = false) Integer id) {

        Post post = postService.getByDeclaration("slug", value);
        boolean flag = true;
        if (post == null)
            flag = false;
        else if(id != null){
            Post oldPost = postService.getByKey(id);
            if (oldPost != null && oldPost.equals(post))
                flag = false;
        }
        return ResponseEntity.ok(new ResponseResult(flag, ""));
    }

}
