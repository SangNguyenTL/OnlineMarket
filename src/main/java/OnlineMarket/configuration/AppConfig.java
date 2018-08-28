package OnlineMarket.configuration;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import OnlineMarket.converter.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.hibernate4.support.OpenSessionInViewInterceptor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import OnlineMarket.thymeleaf.dialect.FilterFormDialect;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "OnlineMarket")
@PropertySource(value = { "classpath:application.properties" })
@EnableScheduling
public class AppConfig extends WebMvcConfigurerAdapter {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	ProductFormatter productFormatter;

	@Autowired
	AddressFormatter addressFormatter;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private Environment environment;

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

		MappingJackson2HttpMessageConverter jacksonMessageConverter = new MappingJackson2HttpMessageConverter();

		converters.add(jacksonMessageConverter);
	}

	@Bean
	public OpenSessionInViewInterceptor openSessionInViewInterceptor(){
		OpenSessionInViewInterceptor openSessionInterceptor = new OpenSessionInViewInterceptor();
		openSessionInterceptor.setSessionFactory(sessionFactory);
		return openSessionInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		OpenSessionInViewInterceptor openSessionInterceptor = new OpenSessionInViewInterceptor();
		openSessionInterceptor.setSessionFactory(sessionFactory);
		registry.addWebRequestInterceptor(openSessionInterceptor);
	}

	@Bean
	public SpringTemplateEngine templateEngine() {

		Set<IDialect> dialects = new LinkedHashSet<>();
		dialects.add(new SpringSecurityDialect());
		dialects.add(new LayoutDialect());
		dialects.add(new FilterFormDialect());

		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setAdditionalDialects(dialects);
		templateEngine.setMessageSource(messageSource());
		return templateEngine;
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(this.applicationContext);
		templateResolver.setCharacterEncoding(environment.getProperty("spring.thymeleaf.characterEncoding"));
		templateResolver.setCacheable(Boolean.parseBoolean(environment.getProperty("spring.thymeleaf.cache")));
		templateResolver.setPrefix("/WEB-INF/views/");
		templateResolver.setSuffix(".html");
		return templateResolver;
	}

	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setCharacterEncoding(environment.getProperty("spring.thymeleaf.characterEncoding"));
		viewResolver.setContentType(environment.getProperty("spring.thymeleaf.contentType"));
		return viewResolver;
	}

	/**
	 * Configure ResourceHandlers to serve static resources like CSS/ Javascript
	 * etc...
	 */

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("ValidationMessages");
		return messageSource;
	}


	/**
	 * Configure Converter to be used. In our example, we need a converter to
	 * convert string values[Roles] to UserProfiles in newUser.jsp
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new ProvinceFormatter());
		registry.addFormatter(productFormatter);
		registry.addFormatter(addressFormatter);
		registry.addFormatter(new RoleFormatter());
		registry.addFormatter(new AttributeValuesFormatter());
	}

	/**
	 * Optional. It's only required when handling '.' in @PathVariables which
	 * otherwise ignore everything after last '.' in @PathVaidables argument. It's a
	 * known bug in Spring [https://jira.spring.io/browse/SPR-6164], still present
	 * in Spring 4.3.0. This is a workaround for this issue.
	 */
	@Override
	public void configurePathMatch(PathMatchConfigurer matcher) {
		matcher.setUseRegisteredSuffixPatternMatch(true);
	}

	/*
	 * Upload Config
	 */
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		return new CommonsMultipartResolver();
	}
}
