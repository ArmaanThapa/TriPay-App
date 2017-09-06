package com.tripayapp.session;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.tripayapp.api.IUserApi;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	protected final Log logger = LogFactory.getLog(this.getClass());

	private final IUserApi userApi;

	public CustomAuthenticationFailureHandler(IUserApi userApi) {
		this.userApi = userApi;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String message = userApi.handleLoginFailure(request, response, exception.getAuthentication(), null,"");
		String redirect = "/Home?msg=" + URLEncoder.encode(message, "UTF-8");
		response.sendRedirect(redirect);
	}
}