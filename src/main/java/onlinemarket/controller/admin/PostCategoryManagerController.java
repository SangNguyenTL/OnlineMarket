package onlinemarket.controller.admin;

import java.util.Date;

import javax.validation.groups.Default;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.PostCategory;
import onlinemarket.model.other.AdvancedValidation;
import onlinemarket.service.PostCategoryService;

@Controller
@RequestMapping("/admin/post-category")
public class PostCategoryManagerController extends MainController{
	@Autowired
	PostCategoryService postCategoryService;
	
	@ModelAttribute
	public void populateFilterForm(ModelMap model) {
		model.put("filterForm", new FilterForm());
		model.put("postCategoryPage", true);
		model.put("pathAdd", "/admin/post-category/add");
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model) {
		model.put("pageTitle", "Post category manager");
		model.put("path", "post-category");
		model.put("result", postCategoryService.list(filterForm));
		model.put("filterForm", filterForm);
		
		return "backend/post-category";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addPage(ModelMap model) {
		model.put("subPageTitle", "Add");
		model.put("description", "Add information of Post Category");
		model.put("pageTitle", "Add new Post Category");
		model.put("path", "post-category-add");
		model.put("action", "add");
		model.put("pathAction", "/admin/post-category/add");
		model.put("postCategory", new PostCategory());
		
		return "backend/post-category-add";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processAddPage(@ModelAttribute("postCategory") @Validated(value = { Default.class,
			AdvancedValidation.CheckSlug.class }) PostCategory postCategory, BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {
		
		String slug = postCategory.getSlug();
		if (StringUtils.isNotBlank(slug)) {
			postCategory.setSlug(slg.slugify(slug));
		}
		
		if (!result.hasErrors()) {
			postCategory.setCreateDate(new Date());
			postCategory.setUpdateDate(new Date());
			postCategoryService.save(postCategory);
			redirectAttributes.addFlashAttribute("success", "");
			return "redirect:/admin/post-category";
		}
		
		model.put("subPageTitle", "Add");
		model.put("description", "Add information of Post Category");
		model.put("pageTitle", "Add new Post category");
		model.put("path", "post-category-add");
		model.put("action", "add");
		model.put("pathAction", "/admin/post-category/add");
		model.put("postCategory", postCategory);
		
		return "backend/post-category-add";
	}
	
	@RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.GET)
	public String updatePage(@PathVariable("id") Integer id, ModelMap model) throws NoHandlerFoundException {

		PostCategory postCategory = postCategoryService.getByKey(id);
		if (postCategory == null)
			throw new NoHandlerFoundException(null, null, null);
		model.put("pageTitle", "Update Post Category");
		model.put("subPageTitle", "Update");
		model.put("description", "Update information of Post Category");
		model.put("path", "post-category-add");
		model.put("action", "update");
		model.put("pathAction", "/admin/post-category/update/"+id);
		model.put("postCategory", postCategory);
		
		return "backend/post-category-add";
		
	}

	@RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.POST)
	public String processUpdatePage(
			@ModelAttribute("postCategory") @Validated(value = { Default.class,
					AdvancedValidation.CheckSlug.class }) PostCategory postCategory,
			@PathVariable("id") Integer id,
			BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) throws NoHandlerFoundException {
		
			PostCategory postCategoryCheck = postCategoryService.getByKey(id);
			if (postCategoryCheck == null)
				throw new NoHandlerFoundException(null, null, null);

			String slug = postCategory.getSlug();
			if (StringUtils.isNotBlank(slug)) {
				postCategory.setSlug(slg.slugify(slug));
			}
			
			postCategory.setUpdateDate(new Date());
			
			if (!result.hasErrors()) {
				postCategory.setUpdateDate(new Date());
				postCategoryService.update(postCategory);
				redirectAttributes.addFlashAttribute("success", "");
				return "redirect:/postCategory/update/"+id;
			}
			
			model.put("pageTitle", "Update post category");
			model.put("subPageTitle", "Update");
			model.put("description", "Update information of post category");
			model.put("path", "post-category-add");
			model.put("action", "update");
			model.put("pathAction", "/admin/post-category/update/"+id);
			model.put("postCategory", postCategory);
			
			return "backend/post-category-add";
	}
	
}
