package com.tripayapp.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

import com.tripayapp.api.ISessionLogApi;

public class SessionLoggingStrategy implements SessionAuthenticationStrategy,
		ApplicationListener<HttpSessionDestroyedEvent> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final ConcurrentSessionControlStrategy concurrentSessionControlStrategy;
	private final ISessionLogApi sessionLogApi;

	public SessionLoggingStrategy(
			ConcurrentSessionControlStrategy concurrentSessionControlStrategy,
			ISessionLogApi sessionLogApi) {
		this.concurrentSessionControlStrategy = concurrentSessionControlStrategy;
		this.sessionLogApi = sessionLogApi;
	}

	@Override
	public void onAuthentication(Authentication authentication,
			HttpServletRequest request, HttpServletResponse response)
			throws SessionAuthenticationException {
		concurrentSessionControlStrategy.onAuthentication(authentication,
				request, response);
		String machineId = request.getHeader("machineId");
		String simId = request.getHeader("simId");
		String ipAddress = request.getHeader("ipAddress");
		logger.info("authentication==>" + authentication);
		logger.info("user logged in from: " + ipAddress);
		logger.info("user logged in machine: " + machineId);
		logger.info("user logged in device: " + simId);
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetailsWrapper) {
			sessionLogApi.logUserLoggedIn(((UserDetailsWrapper) principal)
					.getUser().getId(), request.getSession().getId(),
					request.getRemoteAddr(), machineId, simId);
		}
	}

	@Override
	public void onApplicationEvent(HttpSessionDestroyedEvent event) {
		HttpSession session = event.getSession();
		Object context = session.getAttribute("SPRING_SECURITY_CONTEXT");
		if (context instanceof SecurityContext) {
			SecurityContext securityContext = (SecurityContext) context;
			Authentication authentication = securityContext.getAuthentication();
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetailsWrapper) {
				sessionLogApi.logUserLoggedOut(((UserDetailsWrapper) principal)
						.getUser().getId(), event.getSession().getId());
			}
		}
	}

}
