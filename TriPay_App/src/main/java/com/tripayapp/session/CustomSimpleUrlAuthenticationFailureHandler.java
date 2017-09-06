package com.tripayapp.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tripayapp.repositories.LoginLogRepository;

public class CustomSimpleUrlAuthenticationFailureHandler extends
		SimpleUrlAuthenticationFailureHandler {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;
	
	public static final String LAST_USERNAME_KEY = "LAST_USERNAME";

	private LoginLogRepository loginLogRepository;

	public LoginLogRepository getLoginLogRepository() {
		return loginLogRepository;
	}

	public void setLoginLogRepository(LoginLogRepository loginLogRepository) {
		this.loginLogRepository = loginLogRepository;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		super.onAuthenticationFailure(request, response, exception);
		String usernameParameter = usernamePasswordAuthenticationFilter
				.getUsernameParameter();
		String lastUserName = request.getParameter(usernameParameter);

		HttpSession session = request.getSession(false);
		if (session != null || isAllowSessionCreation()) {
			request.getSession().setAttribute(LAST_USERNAME_KEY, lastUserName);
		}
	}

}
