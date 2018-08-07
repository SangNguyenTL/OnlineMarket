package onlinemarket.component;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
      HttpServletResponse response, Authentication authentication)
      throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }
    
    protected void handle(HttpServletRequest request, 
    	      HttpServletResponse response, Authentication authentication)
    	      throws IOException {
    	  		
    	        String targetUrl = determineTargetUrl(authentication, request);
    	 
    	        if (response.isCommitted()) {
    	        	System.out.println("Response has already been committed. Unable to redirect to "
    	    	              + targetUrl);
    	            return;
    	        }  

    	        redirectStrategy.sendRedirect(request, response, targetUrl);
    	    }
    	 
    	    protected String determineTargetUrl(Authentication authentication, HttpServletRequest request) {
    	        boolean isUser = false;
    	        boolean isAdmin = false;
    	        Collection<? extends GrantedAuthority> authorities
    	         = authentication.getAuthorities();
    	        for (GrantedAuthority grantedAuthority : authorities) {
    	            if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
    	                isUser = true;
    	                break;
    	            } else if (grantedAuthority.getAuthority().contains("ADMIN")) {
    	                isAdmin = true;
    	                break;
    	            }
    	        }

    	        String referrer = request.getParameter("redirect");
    	        if(referrer == null) referrer = "/";

				if (isUser) {
    	            return referrer;
    	        } else if (isAdmin) {
    	            return "/admin";
    	        } else {
    	            throw new IllegalStateException();
    	        }
    	    }
    	 
    	    protected void clearAuthenticationAttributes(HttpServletRequest request) {
    	        HttpSession session = request.getSession(false);
    	        if (session == null) {
    	            return;
    	        }
    	        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    	    }
    	 
    	    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
    	        this.redirectStrategy = redirectStrategy;
    	    }
    	    protected RedirectStrategy getRedirectStrategy() {
    	        return redirectStrategy;
    	    }

}
