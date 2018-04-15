package onlinemarket.thymeleaf.dialect;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;
import org.unbescape.html.HtmlEscape;

import onlinemarket.form.filter.FilterForm;

public class OrderHeadElementProcessor extends AbstractElementTagProcessor {

	private static final String TAG_NAME = "th";
	private static final int PRECEDENCE = 1000;

	private String order;

	private String orderBy;

	private Integer currentPage;

	private String name;

	private String uri;

	private FilterForm filterForm;

	private TreeMap<String, Object> attributes;

	public OrderHeadElementProcessor(final String dialectPrefix) {
		super(TemplateMode.HTML, // This processor will apply only to HTML mode
				dialectPrefix, // Prefix to be applied to name for matching
				TAG_NAME, // Tag name: match specifically this tag
				true, // Apply dialect prefix to tag name
				null, // No attribute name: will match by tag name
				false, // No prefix to be applied to attribute name
				PRECEDENCE); // Precedence (inside dialect's own precedence)
		attributes = new TreeMap<>();
		attributes.put("order", "asc");
		attributes.put("orderBy", "");
		attributes.put("currentPage", 1);
		attributes.put("name", "");
		attributes.put("uri", "");
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {

		filterForm = (FilterForm) context.getVariable("filterForm");

		if (filterForm == null)
			filterForm = new FilterForm();

		processAttributes(tag);

		if (!StringUtils.equalsIgnoreCase(order, "asc") && !StringUtils.equalsIgnoreCase(order, "desc"))
			order = "asc";

		/*
		 * Create the DOM structure that will be substituting our custom tag. The
		 * headline will be shown inside a '<div>' tag, and so this must be created
		 * first and then a Text node must be added to it.
		 */
		final IModelFactory modelFactory = context.getModelFactory();

		final IModel model = modelFactory.createModel();

		constructLink(modelFactory, model, currentPage, name);

		structureHandler.replaceWith(model, false);
	}

	private void constructLink(IModelFactory modelFactory, IModel model, int page, String text) {
		Map<String, String> map = new HashMap<>();
		map.put("href", uri + "/page/" + page + buildQuery());
		String classArrow = "fa fa-arrow-up pull-right";
		if (StringUtils.equals(filterForm.getOrderBy(), orderBy)) {
			map.put("class", "text-info");
			if (StringUtils.equalsIgnoreCase(filterForm.getOrder(), "asc"))
				classArrow = "fa fa-arrow-down pull-right";
		}
		model.add(modelFactory.createOpenElementTag("th"));
		model.add(modelFactory.createOpenElementTag("a", map, AttributeValueQuotes.DOUBLE, false));
		model.add(modelFactory.createText(text));
		model.add(modelFactory.createOpenElementTag("i", "class", classArrow));
		model.add(modelFactory.createCloseElementTag("i"));
		model.add(modelFactory.createCloseElementTag("a"));
		model.add(modelFactory.createCloseElementTag("th"));

	}

	private String buildQuery() {
		StringBuilder sb = new StringBuilder();

		for (Field field : filterForm.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				if (StringUtils.equals(field.getName(), "currentPage"))
					continue;

				Object value = field.get(filterForm);

				if (sb.length() > 0) {
					sb.append('&');
				}

				if (value instanceof String && StringUtils.isEmpty((String) value))
					continue;

				if(StringUtils.equals(field.getName(), "privateGroupSearch")) continue;
				if (StringUtils.equals(field.getName(), "groupSearch")) {

					@SuppressWarnings("unchecked")
					TreeMap<String, String> values = (TreeMap<String, String>) value;
					Iterator<Map.Entry<String, String>> valueI = values.entrySet().iterator();
					while (valueI.hasNext()) {
						Map.Entry<String, String> valueE = valueI.next();
						if(StringUtils.isNotBlank(valueE.getValue()))
						sb.append(valueE.getKey()).append("=").append(URLEncoder.encode(valueE.getValue(), "UTF-8"));
					}

				} else {
					value = value == null ? new String("") : value;

					if (StringUtils.equals(field.getName(), "orderBy")) {
						value = orderBy;
					}

					if (StringUtils.equals(filterForm.getOrderBy(), orderBy)
							&& StringUtils.equals(field.getName(), "order")) {
						value = ((String) value).toLowerCase().equals("asc") ? "desc" : "asc";
					}
					if(StringUtils.isNotBlank(String.valueOf(value)))
					sb.append(URLEncoder.encode(field.getName(), "UTF-8")).append('=')
							.append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
				}
			} catch (UnsupportedEncodingException | IllegalArgumentException | IllegalAccessException e) {
				continue;
			}
		}
		if (sb.length() > 0)
			sb.reverse().append("?").reverse();
		return sb.toString();
	}

	private void processAttributes(IProcessableElementTag tag) {
		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String key = entry.getKey();
			Object str = tag.getAttributeValue(key);
			if (str == null)
				str = entry.getValue();
			Object value = entry.getValue();
			if (value instanceof Integer) {
				try {
					if (str instanceof Integer)
						entry.setValue(str);
					else {
						int val = Integer.parseInt((String) str);
						entry.setValue(val);
					}
				} catch (Exception e) {
					continue;
				}
			} else if (value instanceof String) {
				entry.setValue(HtmlEscape.escapeHtml5(((String) str).trim()));
			}
			try {

				Field field = this.getClass().getDeclaredField(key);

				field.setAccessible(true);

				if (field.getClass().equals(Integer.class)) {
					int iNum = (int) entry.getValue();
					field.set(this, iNum);
				} else {
					field.set(this, entry.getValue());
				}

			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				continue;
			}
		}
	}
}
