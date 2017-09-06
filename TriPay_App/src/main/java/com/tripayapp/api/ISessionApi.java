package com.tripayapp.api;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tripayapp.entity.User;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.UserSessionDTO;
import com.tripayapp.session.UserDetailsWrapper;

public interface ISessionApi {

	void registerNewSession(String sessionId, UserDetailsWrapper principal);

	void removeSession(String tokenKey);

	UserSession getUserSession(String sessionId);
	
	UserSession getActiveUserSession(String sessionId);

	void refreshSession(String sessionId);

	List<UserSession> getAllUserSession(long userId, boolean includeExpiredSessions);

	void expireSession(String sessionId);

	boolean checkActiveSession(User user);

	long countActiveSessions();

	Page<User> findOnlineUsers(Pageable page);

	Page<UserSession> findActiveSessions(Pageable page);

	void registerNewSession(String sessionId, User principal);

	List<UserSessionDTO> getAllActiveUser();

	void clearAllSessionForUser(User user);

}
