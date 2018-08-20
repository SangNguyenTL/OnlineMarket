package OnlineMarket.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;

import OnlineMarket.component.CustomAccessDeniedHandler;
import OnlineMarket.component.CustomAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	private String [] publicUrls = new String [] {
//            "/api/**"
    };

	@Autowired
	CustomAuthenticationSuccessHandler successHandler;
	
	@Autowired
	@Qualifier("customUserDetailsService")
	UserDetailsService userDetailsService;

	@Autowired
	PersistentTokenRepository tokenRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;

	@Autowired
	ConcurrentSessionFilter concurrentSessionFilter;
	
	@Bean
	public SimpleUrlAuthenticationFailureHandler myFailureHandler() {
		return new SimpleUrlAuthenticationFailureHandler();
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Autowired
	SessionRegistry sessionRegistry;

	@Autowired
	SessionAuthenticationStrategy sessionAuthenticationStrategy;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		accessDeniedHandler.setErrorPage("/error");
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        http.addFilterBefore(filter,CsrfFilter.class);

        http.authorizeRequests()
				.antMatchers(
						"/admin/**",
						"/api/rating/**",
						"/api/menu/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
				.antMatchers("/api/image/**", "/user/**").authenticated()
				.anyRequest().permitAll();

        http.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.successHandler(successHandler)
				.failureUrl("/login?error=wrong")
				.permitAll();

        http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.NEVER)
				.sessionAuthenticationStrategy(sessionAuthenticationStrategy)
				.sessionAuthenticationErrorUrl("/login?error=invalidSession")
				.sessionFixation().migrateSession()
				.maximumSessions(1)
				.maxSessionsPreventsLogin(false)
				.expiredUrl("/login?error=expired")
				.sessionRegistry(sessionRegistry);

		http.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.invalidateHttpSession(false)
				.deleteCookies()
				.logoutSuccessUrl("/login?logout")
				.permitAll();

		http.rememberMe()
				.key("uniqueAndSecret")
				.rememberMeParameter("remember-me")
				.tokenRepository(tokenRepository)
				.tokenValiditySeconds(60 * 60 * 24 * 2);

		http.exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler);

		http.csrf()
		        .csrfTokenRepository(new CookieCsrfTokenRepository())
		        .ignoringAntMatchers(publicUrls);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/assets/**");
	}

	@Bean
	public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
		PersistentTokenBasedRememberMeServices tokenBasedService = new PersistentTokenBasedRememberMeServices(
				"remember-me", userDetailsService, tokenRepository);
		return tokenBasedService;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}