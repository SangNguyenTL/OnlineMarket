package onlinemarket.thymeleaf.dialect;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring4.context.SpringContextUtils;
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

	private ServletContext servletContext;

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
		attributes.put("order", "ASC");
		attributes.put("orderBy", "");
		attributes.put("currentPage", 1);
		attributes.put("name", "");
		attributes.put("uri", "");
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		final ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);

		servletContext = appCtx.getBean(ServletContext.class);

		processFilterForm();

		processAttributes(tag);

		if (!StringUtils.equalsIgnoreCase(order.toLowerCase(), "asc")
				|| !StringUtils.equalsIgnoreCase(order.toLowerCase(), "desc"))
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
			map.put("class", "text-infor");
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

				Object value = field.get(filterForm);

				if (value == null)
					continue;

				if (sb.length() > 0) {
					sb.append('&');
				}
				if (StringUtils.equals(filterForm.getOrderBy(), orderBy)) {
					if (field.getName().equals("orderBy")) {
						value = orderBy;
					} else if (StringUtils.equals(field.getName(), "order")) {
						value = ((String) value).toLowerCase().equals("asc") ? "desc" : "asc";
					}
				} else {
					if (StringUtils.equalsIgnoreCase(field.getName(), "orderBy")) {
						value = orderBy;
					} else if (StringUtils.equals(field.getName(), "order")) {
						value = order;
					}
				}
				sb.append(URLEncoder.encode(field.getName(), "UTF-8")).append('=')
						.append(URLEncoder.encode(value.toString(), "UTF-8"));
			} catch (UnsupportedEncodingException | IllegalArgumentException | IllegalAccessException e) {
				continue;
			}
		}
		if (sb.length() > 0)
			sb.reverse().append("?").reverse();
		return sb.toString();
	}

	private void processFilterForm() {
		filterForm = new FilterForm();
		Object valueAttribute;

		for (Field field : filterForm.getClass().getDeclaredFields()) {
			valueAttribute = servletContext.getAttribute(field.getName());
			if (valueAttribute == null)
				continue;
			field.setAccessible(true);
			if (field.getClass().equals(Integer.class)) {
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
		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String key = entry.getKey();
			Object str = tag.getAttributeValue(key);
			if (str == null)
				str = entry.getValue();
			Object value = entry.getValue();
			if (!value.equals(str))
				if (value instanceof Integer) {
					try {
						int val = Integer.parseInt((String) str);
						entry.setValue(val);
					} catch (Exception e) {
						continue;
					}
				} else {
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
