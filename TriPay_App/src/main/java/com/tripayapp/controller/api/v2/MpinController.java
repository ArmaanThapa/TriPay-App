package com.tripayapp.controller.api.v2;

import com.tripayapp.api.ISessionApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.MpinChangeDTO;
import com.tripayapp.model.MpinDTO;
import com.tripayapp.model.SessionDTO;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.error.ChangeMpinError;
import com.tripayapp.model.error.MpinError;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.MpinValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/Api/v2/{role}/{device}/{language}")
public class MpinController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final MpinValidation mpinValidation;
	private final IUserApi userApi;
	private final UserSessionRepository userSessionRepository;
	private final PersistingSessionRegistry persistingSessionRegistry;
	private final ISessionApi sessionApi;

	public MpinController(MpinValidation mpinValidation, IUserApi userApi, UserSessionRepository userSessionRepository,
			PersistingSessionRegistry persistingSessionRegistry, ISessionApi sessionApi) {
		this.mpinValidation = mpinValidation;
		this.userApi = userApi;
		this.userSessionRepository = userSessionRepository;
		this.persistingSessionRegistry = persistingSessionRegistry;
		this.sessionApi = sessionApi;
	}

	@RequestMapping(value = "/SetMpin", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> setNewMpin(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody MpinDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		MpinError error = mpinValidation.validateNewMpin(dto);
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			String sessionId = dto.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (error.isValid()) {
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							dto.setUsername(user.getUsername());
							boolean updated = userApi.setNewMpin(dto);
							if (updated)
								result.setMessage("MPIN Updated");
							else
								result.setMessage("MPIN not updated");
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("User | Set MPIN");
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Failed, Unauthorized user.");
							result.setDetails("Failed, Unauthorized user.");
						}
					}

				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Please, login and try again.");
					result.setDetails("Please, login and try again.");
				}
			} else {
				result.setStatus(ResponseStatus.BAD_REQUEST);
				result.setMessage("Failed, invalid request.");
				result.setDetails(error);
			}
		} else {
			result.setStatus(ResponseStatus.INVALID_HASH);
			result.setMessage("Failed, Please try again later.");
		}

		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);

	}

	@RequestMapping(value = "/ChangeMpin", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> changeOldMpin(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody MpinChangeDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ChangeMpinError error = mpinValidation.validateChangeMpin(dto);
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			String sessionId = dto.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (error.isValid()) {
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							dto.setUsername(user.getUsername());
							if (user.getMpin().equals(dto.getOldMpin().trim())) {
								boolean updated = userApi.changeCurrentMpin(dto);
								result.setStatus(ResponseStatus.SUCCESS);
								if (updated) {
									result.setMessage("MPIN is updated successfully.");
									result.setDetails("MPIN is updated successfully.");
								} else {
									result.setMessage("Failed, to update MPIN. Please try again later.");
									result.setDetails("Failed, to update MPIN. Please try again later.");
								}
							} else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Failed, Old MPIN is incorrect. Please enter correct MPIN.");
								result.setDetails("Failed, invalid request.");
							}
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Failed, Unauthorized user.");
							result.setDetails("Failed, Unauthorized user.");
						}
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Please, login and try again.");
					result.setDetails("Please, login and try again.");
				}
			} else {
				result.setStatus(ResponseStatus.BAD_REQUEST);
				result.setMessage("Failed, invalid request.");
				result.setDetails(error);
			}
		} else {
			result.setStatus(ResponseStatus.INVALID_HASH);
			result.setMessage("Failed, Please try again later.");
		}

		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/VerifyMpin", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> verifyMpin(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody MpinChangeDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			String sessionId = dto.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (userSession != null) {
				UserDTO user = userApi.getUserById(userSession.getUser().getId());
				if (user != null) {
					if (user.getAuthority().contains(Authorities.USER)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						dto.setUsername(user.getUsername());
						if (dto.getOldMpin() != null) {
							if (user.getMpin().equals(dto.getOldMpin())) {
								result.setStatus(ResponseStatus.SUCCESS);
								result.setMessage("MPIN Verified.");
								result.setDetails("MPIN Verified.");
							} else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("MPIN mismatch. Please try again.");
								result.setDetails("MPIN mismatch. Please try again.");
							}
						} else {
							result.setStatus(ResponseStatus.FAILURE);
							result.setMessage("This is not your MPIN. Please enter your correct MPIN.");
							result.setDetails("This is not your MPIN. Please enter your correct MPIN.");
						}
					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("Failed, Unauthorized user.");
						result.setDetails("Failed, Unauthorized user.");
					}
				}
			} else {
				result.setStatus(ResponseStatus.INVALID_SESSION);
				result.setMessage("Please, login and try again.");
				result.setDetails("Please, login and try again.");
			}
		} else {
			result.setStatus(ResponseStatus.INVALID_HASH);
			result.setMessage("Failed, Please try again later.");
		}

		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/ForgotMpin", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> forgotMpinRequest(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SessionDTO session, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(session, hash);
		if (isValidHash) {
			String sessionId = session.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (userSession != null) {
				UserDTO user = userApi.getUserById(userSession.getUser().getId());
				if (user != null) {
					if (user.getAuthority().contains(Authorities.USER)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						boolean isDeleted = userApi.deleteCurrentMpin(user.getUsername());
						if (isDeleted) {
							User u = userApi.findByUserName(user.getUsername());
							sessionApi.clearAllSessionForUser(u);
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Please reset your MPIN.");
							result.setDetails("Please reset your MPIN.");
						} else {
							result.setStatus(ResponseStatus.FAILURE);
							result.setMessage("Please try again later.");
							result.setDetails("Please try again later.");
						}
					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("Failed, Unauthorized user.");
						result.setDetails("Failed, Unauthorized user.");
					}
				}
			} else {
				result.setStatus(ResponseStatus.INVALID_SESSION);
				result.setMessage("Please, login and try again.");
				result.setDetails("Please, login and try again.");
			}
		} else {
			result.setStatus(ResponseStatus.INVALID_HASH);
			result.setMessage("Failed, Please try again later.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}
}
