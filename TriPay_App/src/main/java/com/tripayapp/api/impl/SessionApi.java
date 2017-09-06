package com.tripayapp.api.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.tripayapp.api.ISessionApi;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.UserSessionDTO;
import com.tripayapp.repositories.UserRepository;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.UserDetailsWrapper;
import com.tripayapp.util.ConvertUtil;

@Transactional
public class SessionApi implements ISessionApi, InitializingBean, MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private MessageSource messageSource;
	
	private final UserSessionRepository userSessionRepository;
	private final UserRepository userRepository;
	private final int sessionExpirtyTime;

	public SessionApi(UserSessionRepository userSessionRepository,
			UserRepository userRepository, int sessionExpirtyTime) {
		this.userSessionRepository = userSessionRepository;
		this.userRepository = userRepository;
		this.sessionExpirtyTime = sessionExpirtyTime;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Override
	public void registerNewSession(String sessionId,
			UserDetailsWrapper principal) {
		UserSession userSession = userSessionRepository
				.findBySessionId(sessionId);
		if (userSession == null) {
			userSession = new UserSession();
			userSession.setSessionId(sessionId);
		}
		userSession.setUser(userRepository.findByUsername(principal
				.getUsername()));
		userSession.setLastRequest(new Date());
		userSessionRepository.save(userSession);
	}

	@Override
	public void removeSession(String tokenKey) {
		UserSession userSession = userSessionRepository
				.findBySessionId(tokenKey);
		if (userSession != null) {
			userSessionRepository.delete(userSession);
		}
	}

	@Override
	public boolean checkActiveSession(User user) {
		boolean valid = true;
		if (user != null) {
			UserSession session = userSessionRepository.findByActiveUserSession(user);
			logger.info("session :: " + session);
			if (session != null) {
				logger.info("Expired :: " + session.isExpired());
				if (!session.isExpired()) {
					session.setExpired(true);
					userSessionRepository.save(session);
					logger.info("Session id :: " + session.getId());
				} else {
					logger.info("Session Expired..222!!");
				}
			} else {
				logger.info("Session Expired..!!");
			}
		}
		logger.info("Valid :: " + valid);
		return valid;
	}

	@Override
	public UserSession getUserSession(String sessionId) {
		return userSessionRepository.findBySessionId(sessionId);
	}
	
	@Override
	public UserSession getActiveUserSession(String sessionId) {
		return userSessionRepository.findByActiveSessionId(sessionId);
	}

	@Override
	public void refreshSession(String sessionId) {
		userSessionRepository.refreshSession(sessionId);
	}

	@Override
	public List<UserSession> getAllUserSession(long userId,
			boolean includeExpiredSessions) {
		if (includeExpiredSessions) {
			return userSessionRepository
					.getUserSessionsIncludingExpired(userId);
		}
		return userSessionRepository.getUserSessions(userId);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MINUTE, -sessionExpirtyTime);
		userSessionRepository.deleteExpiredSessions(c.getTime());

	}

	@Override
	public void expireSession(String sessionId) {
		UserSession session = getUserSession(sessionId);
		session.setExpired(true);
		userSessionRepository.save(session);
	}

	@Override
	public long countActiveSessions() {
		return userSessionRepository.countActiveSessions();
	}

	@Override
	public Page<User> findOnlineUsers(Pageable page) {
		return userSessionRepository.findActiveUsers(page);
	}

	@Override
	public Page<UserSession> findActiveSessions(Pageable page) {
		return userSessionRepository.findActiveSessions(page);
	}

	@Override
	public void registerNewSession(String sessionId, User principal) {
		UserSession userSession = userSessionRepository
				.findBySessionId(sessionId);
		if (userSession == null) {
			userSession = new UserSession();
			userSession.setSessionId(sessionId);
		}
		userSession.setUser(userRepository.findByUsername(principal
				.getUsername()));
		userSession.setLastRequest(new Date());
		userSessionRepository.save(userSession);
	}

	@Override
	public List<UserSessionDTO> getAllActiveUser() {
		List<UserSession> userSession = userSessionRepository.findActiveUser();
		return ConvertUtil.convertSessionList(userSession);

	}
	
	@Override
	public void clearAllSessionForUser(User user) {
		List<UserSession> userSessions = userSessionRepository.getUserSessions(user.getId());
		for (UserSession userSession : userSessions) {
			System.err.println(userSession.getSessionId());
			expireSession(userSession.getSessionId());
		}
	}
}
