package OnlineMarket.thymeleaf.dialect;

import java.lang.reflect.Field;
import java.util.*;

import OnlineMarket.model.User;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import OnlineMarket.form.filter.FilterForm;

public class OrderHeadElementProcessor extends AbstractElementTagProcessor {

	private static final String TAG_NAME = "th";
	private static final int PRECEDENCE = 1000;

	private String order;

	private String name;

	private String uri;

	private String classAttribute;

	private FilterForm filterForm;

    private String orderBy;

	private boolean publicState = false;

    OrderHeadElementProcessor(final String dialectPrefix) {
		super(TemplateMode.HTML, // This processor will apply only to HTML mode
				dialectPrefix, // Prefix to be applied to name for matching
				TAG_NAME, // Tag name: match specifically this tag
				true, // Apply dialect prefix to tag name
				null, // No attribute name: will match by tag name
				false, // No prefix to be applied to attribute name
				PRECEDENCE); // Precedence (inside dialect's own precedence)
		uri = "";
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {

		filterForm = (FilterForm) context.getVariable("filterForm");

		User user = (User) context.getVariable("currentUser");

        publicState = user == null || user.getRole() == null || (!user.getRole().getName().equals("ADMIN") && !user.getRole().getName().equals("SUPER_ADMIN"));

		if (filterForm == null)
			filterForm = new FilterForm();

		processAttributes(tag);

		if (!StringUtils.equalsIgnoreCase(filterForm.getOrder(), "asc") || !StringUtils.equalsIgnoreCase(filterForm.getOrder(), "desc"))
			order = "asc";

		/*
		 * Create the DOM structure that will be substituting our custom tag. The
		 * headline will be shown inside a '<div>' tag, and so this must be created
		 * first and then a Text node must be added to it.
		 */
		final IModelFactory modelFactory = context.getModelFactory();

		final IModel model = modelFactory.createModel();

		constructLink(modelFactory, model, filterForm.getCurrentPage(), name);

		structureHandler.replaceWith(model, false);
	}

	private void constructLink(IModelFactory modelFactory, IModel model, int page, String text) {
		Map<String, String> map = new HashMap<>();
		map.put("href", uri + "/page/" + page + buildQuery());
		String classArrow = "fa fa-arrow-up pull-right";
		if (StringUtils.equals(filterForm.getOrderBy(), orderBy)) {
			map.put("class", "text-info");
			if (StringUtils.equalsIgnoreCase(order, "asc"))
				classArrow = "fa fa-arrow-down pull-right";
		}
		model.add(modelFactory.createOpenElementTag("th", "class", classAttribute));
		model.add(modelFactory.createOpenElementTag("a", map, AttributeValueQuotes.DOUBLE, false));
		model.add(modelFactory.createText(text));
		model.add(modelFactory.createOpenElementTag("i", "class", classArrow));
		model.add(modelFactory.createCloseElementTag("i"));
		model.add(modelFactory.createCloseElementTag("a"));
		model.add(modelFactory.createCloseElementTag("th"));

	}

	private String buildQuery() {
		StringBuilder sb;
		Set<String> paramList = new HashSet<>();
		for (Field field : filterForm.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				if (StringUtils.equals(field.getName(), "currentPage"))
					continue;

				Object value = field.get(filterForm);

				if (value instanceof String && StringUtils.isEmpty((String) value))
					continue;

				if(StringUtils.equals(field.getName(), "excludeProperties")) continue;

				if (StringUtils.equals(field.getName(), "groupSearch") && value instanceof TreeMap) {

					@SuppressWarnings("unchecked")
					TreeMap<String, String> values = (TreeMap<String, String>) value;
                    for (Map.Entry<String, String> valueE : values.entrySet()) {
                    	 if(!publicState && (valueE.getKey().equals("state") || valueE.getKey().equals("status") || valueE.getKey().equals("postType"))) continue;
                        if (StringUtils.isNotBlank(valueE.getValue())) {
                            sb = new StringBuilder();
                            paramList.add(sb.append(valueE.getKey()).append("=").append(valueE.getValue()).toString());
                        }
                    }

				} else {
					value = value == null ? "" : value;

					if (StringUtils.equals(field.getName(), "orderBy")) {
						value = orderBy;
					}

					if (StringUtils.equals(filterForm.getOrderBy(), orderBy)
							&& StringUtils.equals(field.getName(), "order") && value instanceof String) {
						value = ((String) value).toLowerCase().equals("asc") ? "desc" : "asc";
					}
					if(StringUtils.isNotBlank(String.valueOf(value))){
                        sb = new StringBuilder();
						paramList.add(sb.append(field.getName()).append('=').append(String.valueOf(value)).toString());
					}

				}
			} catch (IllegalArgumentException | IllegalAccessException ignored) {

			}
		}
		return paramList.size() > 0 ? "?"+StringUtils.join(paramList.iterator(),"&") : "";
	}

	private void processAttributes(IProcessableElementTag tag) {
		uri = tag.getAttributeValue("uri");
		name = tag.getAttributeValue("name");
		classAttribute = tag.getAttributeValue("class");
		orderBy = tag.getAttributeValue("orderBy");
	}
}
