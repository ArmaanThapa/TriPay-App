package com.tripayapp.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.tripayapp.entity.SessionLog;
import com.tripayapp.entity.User;

public interface ISessionLogApi {

	void logUserLoggedIn(long userId, String sessionId, String remoteAddress, String machineId, String simId);

	void logUserLoggedOut(long userId, String sessionId);

	long getTotalOnlineUsers();

	List<SessionLog> getUserHistory(long userId);

	void endUserSession(long userId);

	String getUserAccountActivity();

	String getUserAccountActivity(User u);
	
	boolean checkRequest(String sessionId, HttpServletRequest request);
}
