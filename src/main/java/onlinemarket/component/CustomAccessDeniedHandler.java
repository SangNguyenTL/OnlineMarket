package onlinemarket.component;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl{
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.access.AccessDeniedException accessDeniedException)
			throws IOException, ServletException {
		if (accessDeniedException instanceof MissingCsrfTokenException
				|| accessDeniedException instanceof InvalidCsrfTokenException) {

			if(request.getRequestURI().contains("login")){
				response.sendRedirect(request.getContextPath()+"/login");
			}
		}
		if(StringUtils.equals(request.getContentType(), MediaType.APPLICATION_JSON_VALUE))
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);

	}
}