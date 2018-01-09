package onlinemarket.thymeleaf.dialect;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring4.context.SpringContextUtils;
import org.thymeleaf.templatemode.TemplateMode;
import org.unbescape.html.HtmlEscape;

import onlinemarket.form.filter.FilterForm;

public class PaginationElementTagProcessor extends AbstractElementTagProcessor{

    private static final String TAG_NAME = "pagination";
    private static final int PRECEDENCE = 1000;
    
    private String uri;
    private Integer currentPage;
    private Integer totalPages;
    private Integer displayedPages;
    private String previous;
    private String next;
	
    private ServletContext servletContext;
    
    private FilterForm filterForm;
    
    private TreeMap<String, Object> attributes;
    
	public PaginationElementTagProcessor(final String dialectPrefix) {
		super(
	            TemplateMode.HTML, // This processor will apply only to HTML mode
	            dialectPrefix,     // Prefix to be applied to name for matching
	            TAG_NAME,          // Tag name: match specifically this tag
	            true,              // Apply dialect prefix to tag name
	            null,              // No attribute name: will match by tag name
	            false,             // No prefix to be applied to attribute name
	            PRECEDENCE);       // Precedence (inside dialect's own precedence)
		attributes = new TreeMap<>();
		attributes.put("uri", "");
		attributes.put("currentPage", 1);
		attributes.put("totalPages", 1);
		attributes.put("displayedPages", 7);
		attributes.put("previous", "Previous");
		attributes.put("next", "Next");
	}

	@Override
	protected void doProcess(
			ITemplateContext context,
			IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		
		final ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
		
		servletContext = appCtx.getBean(ServletContext.class);
		
		processFilterForm();

		processAttributes(tag);
        
        /*
         * Create the DOM structure that will be substituting our custom tag.
         * The headline will be shown inside a '<div>' tag, and so this must
         * be created first and then a Text node must be added to it.
         */
        final IModelFactory modelFactory = context.getModelFactory();

        final IModel model = modelFactory.createModel();

        model.add(modelFactory.createOpenElementTag("ul", "class", "pagination pagination-sm m-a-0"));
        
        if(currentPage < 2)
        	constructLink(modelFactory, model, 1, previous, "disabled", true);
        else
            constructLink(modelFactory, model, currentPage-1, previous, null, false);
        
        int step = (int) Math.floor(Float.valueOf(displayedPages / 2));
        
        if(currentPage + displayedPages < totalPages && currentPage > step)
	        for(int itr=-step; itr <= currentPage + displayedPages ; itr+=currentPage) {
	        	if(itr == currentPage)
	        		constructLink(modelFactory, model, itr, String.valueOf(itr), "active", false);
	        	else
	        		constructLink(modelFactory, model, itr, String.valueOf(itr), "", false);
	        }
        else if(totalPages < displayedPages)
	        for(int itr=1; itr <= totalPages ; itr+=currentPage) {
	        	if(itr == currentPage)
	        		constructLink(modelFactory, model, itr, String.valueOf(itr), "active", false);
	        	else
	        		constructLink(modelFactory, model, itr, String.valueOf(itr), "", false);
	        }
        else
	        for(int itr=1; itr <= currentPage + displayedPages ; itr+=currentPage) {
	        	if(itr == currentPage)
	        		constructLink(modelFactory, model, itr, String.valueOf(itr), "active", false);
	        	else
	        		constructLink(modelFactory, model, itr, String.valueOf(itr), "", false);
	        }
        
        if(currentPage+displayedPages>=totalPages)
        	constructLink(modelFactory, model, currentPage+displayedPages, next, "disabled", true);
        else if(displayedPages < totalPages)
            constructLink(modelFactory, model, currentPage+displayedPages, next, null , false);
        
        model.add(modelFactory.createCloseElementTag("ul"));
        
        structureHandler.replaceWith(model, false);

	}
	
    private void constructLink(IModelFactory modelFactory, IModel model,int page, String text, String className, boolean disable) {
    	   	
    	model.add(modelFactory.createOpenElementTag("li", "class", className));
    	model.add(modelFactory.createOpenElementTag("a", "href", disable ? "#" : uri+"/page/"+page+buildQuery()));
    	model.add(modelFactory.createText(text));
    	model.add(modelFactory.createCloseElementTag("a"));
    	model.add(modelFactory.createCloseElementTag("li"));
    	
    }
    
    private String buildQuery() {
    	StringBuilder sb = new StringBuilder();

    	for(Field field : filterForm.getClass().getDeclaredFields()) {
    		field.setAccessible(true);
    		try {
    		
    			Object value = field.get(filterForm);
    		
	    		if(value==null) continue;
	    		
	    		if(sb.length() > 0){
	  	          sb.append('&');
	  	      	}
			
				sb.append(URLEncoder.encode(field.getName(), "UTF-8")).append('=').append(URLEncoder.encode(value.toString(), "UTF-8"));
			} catch (UnsupportedEncodingException | IllegalArgumentException | IllegalAccessException e) {
				continue;
			}
    	}
    	if(sb.length() > 0) sb.reverse().append("?").reverse();
    	return sb.toString();
    }
    
    private void processFilterForm() {
		filterForm = new FilterForm();
		Object valueAttribute;
		
		for(Field field : filterForm.getClass().getDeclaredFields()) {
			valueAttribute =  servletContext.getAttribute(field.getName());
			if(valueAttribute == null) continue;
			field.setAccessible(true);
			if(field.getClass().equals(Integer.class)) {
				valueAttribute = Integer.parseInt((String) valueAttribute);
				try {
					field.set(filterForm, valueAttribute);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					continue;
				}
			}
		}
    }
	
	private void processAttributes(IProcessableElementTag tag) {
        for ( Map.Entry<String, Object> entry : attributes.entrySet()) {
        	String key = entry.getKey();
        	Object str = tag.getAttributeValue(key);
        	if(str == null) str = entry.getValue();
        	Object value = entry.getValue();
        	if(!value.equals(str))
        	if(value instanceof Integer) {
        		try {
					int val = Integer.parseInt((String) str);
					entry.setValue(val);
				} catch (Exception e) {
					continue;
				}
        	}else {
    			entry.setValue(HtmlEscape.escapeHtml5(((String) str).trim()));
        	}
        	try {
				Field field = this.getClass().getDeclaredField(key);
				field.setAccessible(true);
				if(field.getClass().equals(Integer.class)) {
					int iNum = (int) entry.getValue();
					field.set(this, iNum);
				}else {
					field.set(this, entry.getValue());
				}
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				continue;
			}
		}
	}
}
