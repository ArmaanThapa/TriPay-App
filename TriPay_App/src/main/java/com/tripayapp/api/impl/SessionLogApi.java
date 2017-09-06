package com.tripayapp.api.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.tripayapp.api.ISessionApi;
import com.tripayapp.api.ISessionLogApi;
import com.tripayapp.entity.SessionLog;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserSession;
import com.tripayapp.repositories.SessionLogRepository;
import com.tripayapp.util.AuthenticationUtil;
import com.tripayapp.util.TimeDifferenceCalUtil;

public class SessionLogApi implements ISessionLogApi, MessageSourceAware {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private MessageSource messageSource;
	
	private final SessionLogRepository sessionLogRepository;
	private final ISessionApi sessionApi;

	public SessionLogApi(SessionLogRepository sessionLogRepository,
			ISessionApi sessionApi) {
		this.sessionLogRepository = sessionLogRepository;
		this.sessionApi = sessionApi;
	}
	
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public void logUserLoggedIn(long userId, String sessionId,
			String remoteAddress, String machineId, String simId) {
		logger.debug("Logging user Logged in:" + userId);
		SessionLog s = new SessionLog();
		s.setUserId(userId);
		s.setLoggedIn(new Date());
		s.setSessionId(sessionId);
		s.setRemoteAddress(remoteAddress);
		s.setMachineId(machineId);
		s.setSimId(simId);
		sessionLogRepository.save(s);
	}

	@Override
	public void logUserLoggedOut(long userId, String sessionId) {
		SessionLog s = sessionLogRepository.findBySessionId(sessionId);
		if (s != null && s.getUserId() == userId) {
			s.setUserId(userId);
			s.setLoggedOut(new Date());
			s.setSessionId(sessionId);
			sessionLogRepository.save(s);
		}
	}

	@Override
	public long getTotalOnlineUsers() {
		return sessionApi.countActiveSessions();
	}

	@Override
	public void endUserSession(long userId) {
		List<UserSession> sessions = sessionApi
				.getAllUserSession(userId, false);
		for (UserSession s : sessions) {
			sessionApi.expireSession(s.getSessionId());
		}
	}

	private SessionLog findUserSessionById(String sessionId) {
		return sessionLogRepository.findBySessionId(sessionId);
	}

	@Override
	public List<SessionLog> getUserHistory(final long userId) {
		Page<SessionLog> sessionLogs = sessionLogRepository.findAll(
				new Specification<SessionLog>() {

					@Override
					public Predicate toPredicate(Root<SessionLog> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						return cb.equal(root.get("userId"), userId);
					}
				}, new PageRequest(0, 10, new Sort(new Order(Direction.DESC,
						"id"))));

		return sessionLogs.getContent();
	}

	@Override
	public String getUserAccountActivity() {
		SessionLog login = getUserSessionLogin(AuthenticationUtil
				.getCurrentUser().getId());

		if (login == null) {
			return "None";
		}
		String logout = getUserSessionLogOut(AuthenticationUtil
				.getCurrentUser().getId(), login.getSessionId());

		if (logout == null || logout.isEmpty()) {
			return "None";
		}

		TimeDifferenceCalUtil timeDiff = new TimeDifferenceCalUtil(logout,
				login.getLoggedIn().toString());
		return timeDiff.getTimeDifference();
	}

	@Override
	public String getUserAccountActivity(User u) {
		SessionLog login = getUserSessionLogin(u.getId());

		if (login == null) {
			return "None";
		}
		String logout = getUserSessionLogOut(u.getId(), login.getSessionId());

		if (logout == null || logout.isEmpty()) {
			return "None";
		}

		TimeDifferenceCalUtil timeDiff = new TimeDifferenceCalUtil(logout,
				login.getLoggedIn().toString());
		return timeDiff.getTimeDifference();
	}

	public SessionLog getUserSessionLogin(long userId) {
		List<UserSession> sessions = sessionApi
				.getAllUserSession(userId, false);
		if (sessions.size() > 0) {
			return findUserSessionById(sessions.get(0).getSessionId());
		}

		return null;
	}

	public String getUserSessionLogOut(long userId, String sessionId) {
		List<SessionLog> sl = getSessionLogs(userId);

		for (SessionLog log : sl) {
			if (!log.getSessionId().equals(sessionId)) {
				return (log.getLoggedOut() == null ? log.getLoggedIn()
						.toString() : log.getLoggedOut().toString());
			}
		}
		return null;
	}

	public List<SessionLog> getSessionLogs(final long userId) {
		Page<SessionLog> sl = sessionLogRepository.findAll(
				new Specification<SessionLog>() {

					@Override
					public Predicate toPredicate(Root<SessionLog> root,
							CriteriaQuery<?> cq, CriteriaBuilder cb) {
						return cb.equal(root.get("userId"), userId);
					}
				}, new PageRequest(0, 5, new Sort(new Order(Direction.DESC,
						"loggedIn"))));
		return sl.getContent();
	}

	@Override
	public boolean checkRequest(String sessionId, HttpServletRequest request) {
		boolean valid = true;
		SessionLog sessionLog = sessionLogRepository.findBySessionId(sessionId);
		String machineId = request.getHeader("machineId");
		String simId = request.getHeader("simId");
		String ipAddress = request.getHeader("ipAddress");
		logger.info("Header Machine ID :: " + machineId);
		logger.info("Header IP Address :: " + ipAddress);
		logger.info("Header SIM ID :: " + simId);
		logger.info("Machine ID :: " + sessionLog.getMachineId());
		logger.info("IP Address :: " + sessionLog.getRemoteAddress());
		logger.info("SIM ID :: " + sessionLog.getSimId());
//		if (ipAddress == null) {
//			if (!machineId.equals(sessionLog.getMachineId())) {
//				valid = false;
//			}
//			if (!simId.equals(sessionLog.getSimId())) {
//				valid = false;
//			}
//		} else {
//			if (!ipAddress.equals(sessionLog.getRemoteAddress())) {
//				valid = false;
//			}
//		}
		return true;
	}

}
