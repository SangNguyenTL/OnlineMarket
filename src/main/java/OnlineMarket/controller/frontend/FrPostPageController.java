package OnlineMarket.controller.frontend;

import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Comment;
import OnlineMarket.model.Post;
import OnlineMarket.result.ResultObject;
import OnlineMarket.service.CommentService;
import OnlineMarket.service.PostService;
import OnlineMarket.service.UserService;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.post.PostNotFoundException;
import OnlineMarket.util.exception.user.UserNotFoundException;
import OnlineMarket.util.group.AdvancedValidation;
import OnlineMarket.util.other.CommentState;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

@Controller
@RequestMapping("")
@SessionAttributes({ "comment" })
public class FrPostPageController extends MainController {

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @ModelAttribute("comment")
    public Comment getCommentForm(){
        return new Comment();
    }

    @Override
    protected void addMeta(ModelMap modelMap) {
        PrettyTime prettyTime = new PrettyTime();
        generateBreadcrumbs();
        FilterForm filterForm = new FilterForm();
        filterForm.setOrderBy("createDate");
        filterForm.setOrder("desc");
        filterForm.getGroupSearch().put("status", String.valueOf(CommentState.ACTIVE.getId()));
        modelMap.put("prettyTime", prettyTime);
        modelMap.put("filterForm", filterForm);
        modelMap.put("relativePath", relativePath);
    }

    @RequestMapping(value = "/{postType:(?:post|page)}/{slug:[\\d\\w-]+}", method = RequestMethod.GET)
    public String postPage(@PathVariable("postType") String postType, @PathVariable("slug") String slug,
                           @ModelAttribute("filterForm") FilterForm filterForm,
                           HttpServletRequest request,
                           ModelMap model)throws NoHandlerFoundException {
        relativePath = "/"+postType+"/"+slug;
        Post post = postService.getByDeclaration("slug", slug);
        if(post == null || post.getStatus() != 0) throw new NoHandlerFoundException(request.getMethod(),request.getRequestURL().toString(),null);

        if(postType.equals("post")){
            breadcrumbs.add(new String[]{"/post-category", "List post"});
            if(post.getPostCategory()!= null)
            breadcrumbs.add(new String[]{"/post-category/"+ post.getPostCategory().getSlug(), post.getPostCategory().getName()});
            model.put("pathAction", relativePath+"/comment");
            Comment comment = (Comment) model.get("comment");
            ResultObject<Comment> resultObject = commentService.listByPost(post, filterForm);
            comment.setUser(currentUser);
            comment.setPost(post);
            model.put("comment", comment);
            model.put("result", resultObject);
        }

        model.put("pageTitle", post.getTitle());
        model.put("post", post);

        return "frontend/post";
    }
    @RequestMapping(value = "/post/{slug:[\\d\\w-]+}/page/{page:\\d+}", method = RequestMethod.GET)
    public String postPagePagination(@PathVariable("slug") String slug,
                           @ModelAttribute("filterForm") FilterForm filterForm,
                            @PathVariable("page") Integer page,
                           HttpServletRequest request,
                           ModelMap model)throws NoHandlerFoundException {
        relativePath = "/post/"+slug;
        Post post = postService.getByDeclaration("slug", slug);
        if(post == null || post.getStatus() != 0) throw new NoHandlerFoundException(request.getMethod(),request.getRequestURL().toString(),null);

        breadcrumbs.add(new String[]{"/post-category", "List post"});
        breadcrumbs.add(new String[]{"/post-category/"+ post.getPostCategory().getSlug(), post.getPostCategory().getName()});
        model.put("pathAction", relativePath+"/comment");
        Comment comment = (Comment) model.get("comment");
        filterForm.setCurrentPage(page);
        ResultObject<Comment> resultObject = commentService.listByPost(post, filterForm);
        comment.setUser(currentUser);
        comment.setPost(post);
        model.put("comment", comment);
        model.put("result", resultObject);


        model.put("pageTitle", post.getTitle());
        model.put("post", post);

        return "frontend/post";
    }

    @RequestMapping(value = "/post/{slug:[\\d\\w-]+}/comment", method = RequestMethod.POST)
    public String addComment(@Validated({Default.class, AdvancedValidation.AddNew.class}) @ModelAttribute("comment") Comment comment, BindingResult result,
                             @PathVariable("slug") String slug,
                             HttpServletRequest request, RedirectAttributes redirectAttributes, final SessionStatus sessionStatus) throws NoHandlerFoundException {

        relativePath = "/post/"+slug;
        Post post = postService.getByDeclaration("slug", slug);
        if(post == null || post.getStatus() != 0) throw new NoHandlerFoundException(request.getMethod(),request.getRequestURL().toString(),null);

        try {
            if (currentUser == null) throw new CustomException("You must be login.");
            redirectAttributes.addFlashAttribute("addComment", true);
            if(!result.hasErrors()){
                commentService.save(comment);
                redirectAttributes.addFlashAttribute("success", true);
                sessionStatus.setComplete();
                return "redirect:"+relativePath+"#comment-box";
            }


        } catch (CustomException|UserNotFoundException|PostNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.comment", result);


        return "redirect:"+relativePath+"#comment-box";
    }
}
