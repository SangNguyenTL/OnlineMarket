package OnlineMarket.thymeleaf.dialect;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

import OnlineMarket.result.ResultObject;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import OnlineMarket.form.filter.FilterForm;

public class PaginationElementTagProcessor extends AbstractElementTagProcessor{

    private static final String TAG_NAME = "pagination";
    private static final int PRECEDENCE = 1000;
    
    private String uri;
    private Integer displayedPages;
    private String previous;
    private String next;
    
    
    private FilterForm filterForm;
    
	PaginationElementTagProcessor(final String dialectPrefix) {
		super(
	            TemplateMode.HTML, // This processor will apply only to HTML mode
	            dialectPrefix,     // Prefix to be applied to name for matching
	            TAG_NAME,          // Tag name: match specifically this tag
	            true,              // Apply dialect prefix to tag name
	            null,              // No attribute name: will match by tag name
	            false,             // No prefix to be applied to attribute name
	            PRECEDENCE);       // Precedence (inside dialect's own precedence)
	}

	@Override
	protected void doProcess(
			ITemplateContext context,
			IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		
		filterForm = (FilterForm) context.getVariable("filterForm");
		
		if(filterForm == null) filterForm = new FilterForm();

        ResultObject resultObject = (ResultObject) context.getVariable("result");

		if(resultObject == null) resultObject = new ResultObject();

        Integer currentPage = filterForm.getCurrentPage();

        Integer totalPages = resultObject.getTotalPages();

		processAttributes(tag);
        
        /*
         * Create the DOM structure that will be substituting our custom tag.
         * The headline will be shown inside a '<div>' tag, and so this must
         * be created first and then a Text node must be added to it.
         */
        final IModelFactory modelFactory = context.getModelFactory();

        final IModel model = modelFactory.createModel();

        model.add(modelFactory.createOpenElementTag("ul", "class", "pagination pagination-sm m-a-0"));
        
        if(currentPage > 1 && totalPages > 2)
            constructLink(modelFactory, model, currentPage -1, previous, null, false);
        
        int step = displayedPages / 2;
        
        if(currentPage >= displayedPages) {
    		for(int itr = currentPage - step; itr <= displayedPages; itr+=1) {
    			if(itr == currentPage)
	        		constructLink(modelFactory, model, itr, String.valueOf(itr), "active", true);
	        	else
	        		constructLink(modelFactory, model, itr, String.valueOf(itr), "", false);
    		}
        }else {
        	if(totalPages <= displayedPages)
	        	for(int itr = 1; itr <= totalPages; itr+=1) {
	    			if(itr == currentPage)
		        		constructLink(modelFactory, model, itr, String.valueOf(itr), "active", true);
		        	else
		        		constructLink(modelFactory, model, itr, String.valueOf(itr), "", false);
	    		}
        	else
        		if(currentPage < step)
        			for(int itr = 1; itr <= displayedPages; itr+=1) {
    	    			if(itr == currentPage)
    		        		constructLink(modelFactory, model, itr, String.valueOf(itr), "active", true);
    		        	else
    		        		constructLink(modelFactory, model, itr, String.valueOf(itr), "", false);
    	    		}
        		else
        			for(int itr = 1; itr <= displayedPages; itr+=1) {
    	    			if(itr == currentPage)
    		        		constructLink(modelFactory, model, itr, String.valueOf(itr), "active", true);
    		        	else
    		        		constructLink(modelFactory, model, itr, String.valueOf(itr), "", false);
    	    		}
        }
        
        if(currentPage < totalPages)
            constructLink(modelFactory, model, currentPage +1, next, null , false);
        
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
		Set<String> paramList = new HashSet<>();
    	for(Field field : filterForm.getClass().getDeclaredFields()) {
    		field.setAccessible(true);
    		try {
    			if(StringUtils.equals(field.getName(), "currentPage")) continue;
    		
    			Object value = field.get(filterForm);

    			if(value instanceof String && StringUtils.isEmpty((String) value)) continue;

	  	      	if( StringUtils.equals(field.getName(), "excludeProperties")) continue;

	    		if (StringUtils.equals(field.getName(), "groupSearch") && value instanceof TreeMap) {

					@SuppressWarnings("unchecked")
					TreeMap<String, String> values = (TreeMap) value;
					for (Map.Entry<String, String> valueE : values.entrySet()) {
						if (StringUtils.isNotBlank(valueE.getValue())) {
							sb = new StringBuilder();
							if(filterForm.getExcludeProperties().contains(valueE.getKey())) continue;
							paramList.add(sb.append("groupSearch[").append(valueE.getKey()).append("]").append("=").append(valueE.getValue()).toString());
						}
					}

				} else {
					value = value == null ? "" : value;
					sb = new StringBuilder();
					if(StringUtils.isNotBlank(String.valueOf(value))){
						paramList.add(sb.append(field.getName()).append('=').append(value.toString()).toString());
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException ignored) {

			}
    	}
    	return paramList.size() > 0 ? "?"+StringUtils.join(paramList.iterator(),"&") : "";
    }
	
	private void processAttributes(IProcessableElementTag tag) {

		try{
			displayedPages =  Integer.parseInt(tag.getAttributeValue("displayedPages"));
		}catch (NumberFormatException ex){
			displayedPages = 7;
		}
		previous = returnDefault(tag.getAttributeValue("previous"), "<");
		next = returnDefault(tag.getAttributeValue("next"), ">");
		uri = returnDefault(tag.getAttributeValue("uri"), "/");
	}

	private String returnDefault(String str, String strDefault){
	    return str == null ? strDefault : str;
    }
}
