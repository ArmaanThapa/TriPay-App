package com.tripayapp.controller.api.v1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tripayapp.model.*;
import com.tripayapp.model.error.LoginError;
import com.tripayapp.util.CommonUtil;
import com.tripayapp.validation.LoginValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;

import com.tripayapp.api.ISessionApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.error.AuthenticationError;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.SessionLoggingStrategy;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}")
public class LoginController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final IUserApi userApi;
	private final AuthenticationManager authenticationManager;
	private final UserSessionRepository userSessionRepository;
	private final SessionLoggingStrategy sessionLoggingStrategy;
	private final ISessionApi sessionApi;
	private final LoginValidation loginValidation;

	public LoginController(IUserApi userApi, AuthenticationManager authenticationManager,
			UserSessionRepository userSessionRepository, SessionLoggingStrategy sessionLoggingStrategy,
			ISessionApi sessionApi,LoginValidation loginValidation) {
		this.userApi = userApi;
		this.authenticationManager = authenticationManager;
		this.userSessionRepository = userSessionRepository;
		this.sessionLoggingStrategy = sessionLoggingStrategy;
		this.sessionApi = sessionApi;
		this.loginValidation = loginValidation;
	}

    @RequestMapping(value = "/Login", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> userLogin(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody LoginDTO login, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(login, hash);
		logger.info("Login: Hash " + isValidHash);
		LoginError error = loginValidation.checkLoginValidation(login);
		if (error.isSuccess()) {
			logger.info("IsValidHash ");
				logger.info("Is Valid version");
				try {
					User user = userApi.findByUserName(login.getUsername());
					if (user != null) {
						if (user.getAuthority().contains(Authorities.BLOCKED)) {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Account is blocked");
							result.setDetails(null);
							return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
						} else if(user.getAuthority().contains(Authorities.LOCKED)) {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Account is locked,contact Customer-Care");
							result.setDetails(null);
							return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
						} else if (role.equalsIgnoreCase("User")) {
							if (user.getAuthority().contains(Authorities.USER)) {
								if (!login.isValidate()) {
									boolean validDevice = userApi.isValidLastLoginDevice(login.getIpAddress(), user);
									validDevice = true;
									if (validDevice) {
										if (sessionApi.checkActiveSession(user)) {
											AuthenticationError auth = authentication(login,
													request);
											if (auth.isSuccess()) {
												try {
													String gcmId = login.getRegistrationId();
													if (gcmId != null) {
														userApi.updateGcmId(gcmId, user.getUsername());
													}
												} catch (NullPointerException e) {
													e.printStackTrace();
												}
												user.setMobileToken(CommonUtil.generateNDigitNumericString(6));
												//After successful verification of otp
												Map<String, Object> detail = new HashMap<String, Object>();
												Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
												sessionLoggingStrategy.onAuthentication(authentication, request, response);
												UserSession userSession = userSessionRepository.findByActiveSessionId(
														RequestContextHolder.currentRequestAttributes().getSessionId());
												logger.info("::" + RequestContextHolder.currentRequestAttributes());
												UserDTO activeUser = userApi.getUserById(userSession.getUser().getId());
												result.setStatus(ResponseStatus.SUCCESS);
												result.setMessage("Login successful.");
												detail.put("sessionId", userSession.getSessionId());
												detail.put("userDetail", activeUser);
												detail.put("accountDetail", user.getAccountDetail());
												result.setDetails(detail);
												return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
											} else {
												result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
												result.setMessage(auth.getMessage());
												result.setDetails(null);
												return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
											}
										}
									} else {
										userApi.requestNewLoginDevice(user);
										result.setStatus(ResponseStatus.NEW_DEVICE);
										result.setMessage("OTP sent to ::" + user.getUsername());
										return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
									}
								}else {
									 boolean isVerified = userApi.isValidLoginToken(login.getMobileToken(),user);
									if(isVerified) {
										if (sessionApi.checkActiveSession(user)) {
											AuthenticationError auth = authentication(login,
													request);
											if (auth.isSuccess()) {
												try {
													String gcmId = login.getRegistrationId();
													if (gcmId != null) {
														userApi.updateGcmId(gcmId, user.getUsername());
													}
												} catch (NullPointerException e) {

												}
												Map<String, Object> detail = new HashMap<String, Object>();
												Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
												sessionLoggingStrategy.onAuthentication(authentication, request, response);
												UserSession userSession = userSessionRepository.findByActiveSessionId(
														RequestContextHolder.currentRequestAttributes().getSessionId());
												logger.info("::" + RequestContextHolder.currentRequestAttributes());
												UserDTO activeUser = userApi.getUserById(userSession.getUser().getId());
												result.setStatus(ResponseStatus.SUCCESS);
												result.setMessage("Login successful.");
												detail.put("sessionId", userSession.getSessionId());
												detail.put("userDetail", activeUser);
												detail.put("accountDetail", user.getAccountDetail());
												result.setDetails(detail);
												return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
											} else {
												result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
												result.setMessage(auth.getMessage());
												result.setDetails(null);
												return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
											}
										}
									}else {
										result.setStatus(ResponseStatus.BAD_REQUEST);
										result.setMessage("Not a valid OTP");
									}
								}
							}
						} else if (role.equalsIgnoreCase("Admin")) {
							if (user.getAuthority().contains(Authorities.ADMINISTRATOR)) {
								AuthenticationError auth = authentication(login, request);
								if (auth.isSuccess()) {
									Map<String, Object> detail = new HashMap<String, Object>();
									Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
									sessionLoggingStrategy.onAuthentication(authentication, request, response);
									UserSession userSession = userSessionRepository.findByActiveSessionId(
											RequestContextHolder.currentRequestAttributes().getSessionId());
									logger.info("::" + RequestContextHolder.currentRequestAttributes().getSessionId());
									UserDTO activeUser = userApi.getUserById(userSession.getUser().getId());
									result.setStatus(ResponseStatus.SUCCESS);
									result.setMessage("Login successful.");
									detail.put("sessionId", userSession.getSessionId());
									detail.put("userDetail", activeUser);
									detail.put("accountDetail", user.getAccountDetail());
									result.setDetails(detail);
									return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
								} else {
									result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
									result.setMessage(auth.getMessage());
									result.setDetails(null);
									return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
								}
							}
						} else if (role.equalsIgnoreCase("Merchant")) {
							if (user.getAuthority().contains(Authorities.MERCHANT)) {
								AuthenticationError auth = authentication(login, request);
								if (auth.isSuccess()) {
									Map<String, Object> detail = new HashMap<String, Object>();
									Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
									sessionLoggingStrategy.onAuthentication(authentication, request, response);
									UserSession userSession = userSessionRepository.findByActiveSessionId(
											RequestContextHolder.currentRequestAttributes().getSessionId());
									logger.info("::" + RequestContextHolder.currentRequestAttributes().getSessionId());
									UserDTO activeUser = userApi.getUserById(userSession.getUser().getId());
									result.setStatus(ResponseStatus.SUCCESS);
									result.setMessage("Login successful.");
									detail.put("sessionId", userSession.getSessionId());
									detail.put("userDetail", activeUser);
									detail.put("accountDetail", user.getAccountDetail());
									result.setDetails(detail);
									return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
								} else {
									result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
									result.setMessage(auth.getMessage());
									result.setDetails(null);
									return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
								}
							}
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Failed, Unauthorized user.");
							result.setDetails(null);
							return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
						}
					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("Failed, Unauthorized user.");
						result.setDetails(null);
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.setStatus(ResponseStatus.BAD_REQUEST);
					result.setMessage("Failed, invalid request.");
					result.setDetails("Failed, invalid request.");
					return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
				}

		}else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage(error.getMessage());
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/Logout", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> logoutUserApi(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SessionDTO session, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(session, hash);

		if (isValidHash) {
			try {
				UserSession userSession = userSessionRepository.findBySessionId(session.getSessionId());
				if (userSession != null) {
					sessionApi.expireSession(session.getSessionId());
					result.setStatus(ResponseStatus.SUCCESS);
					result.setMessage("User logout successful");
					result.setDetails("Session Out");
					return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Please, login and try again.");
					result.setDetails("Please, login and try again.");
					return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
				}
			} catch (Exception e) {
				result.setStatus(ResponseStatus.BAD_REQUEST);
				result.setMessage("Failed, invalid request.");
				result.setDetails("Failed, invalid request.");
				return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
			}
		} else {
			result.setStatus(ResponseStatus.INVALID_HASH);
			result.setMessage("Failed, Please try again later.");
			return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
		}
	}

	private AuthenticationError authentication(LoginDTO dto, HttpServletRequest request)
			throws ServletException, IOException, Exception {
		AuthenticationError error = new AuthenticationError();
		Authentication auth = null;
		UsernamePasswordAuthenticationToken token = null;
		try {
			token = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
			logger.info("TOKEN ::" + String.valueOf(token.getPrincipal()));
			auth = authenticationManager.authenticate(token);
			logger.info("AUTH NAME :: " + auth.getName());
			logger.info("AUTH CREDENTIALS :: " + auth.getCredentials());
			logger.info("AUTH AUTHORITY :: " + auth.getAuthorities());
			SecurityContext securityContext = SecurityContextHolder.getContext();
			if (auth.isAuthenticated()) {
				logger.info("AUTH :: Authenticated");
				securityContext.setAuthentication(auth);
				SecurityContextHolder.getContext().setAuthentication(auth);
				HttpSession session = request.getSession(true);
				session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
				error.setSuccess(true);
				error.setMessage("Login successful.");
				userApi.handleLoginSuccess(request, null, auth, String.valueOf(token.getPrincipal()),dto.getIpAddress());
				return error;
			} else {
				logger.info("AUTH :: NOT Authenticated");
				error.setSuccess(false);
				error.setMessage(userApi.handleLoginFailure(request, null, auth, String.valueOf(token.getPrincipal()),dto.getIpAddress()));
				SecurityContextHolder.getContext().setAuthentication(null);
				return error;
			}
		} catch (Exception e) {
			e.printStackTrace();
			SecurityContextHolder.getContext().setAuthentication(null);
			error.setSuccess(false);
			error.setMessage(userApi.handleLoginFailure(request, null, auth, String.valueOf(token.getPrincipal()),dto.getIpAddress()));
			return error;
		}
	}

}
