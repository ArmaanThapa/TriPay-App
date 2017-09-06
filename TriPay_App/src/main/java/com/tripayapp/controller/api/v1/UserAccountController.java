package com.tripayapp.controller.api.v1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.InviteFriendsDTO;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.error.InviteFriendsError;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.InviteFriendValidation;

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}/InviteFriends")
public class UserAccountController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final InviteFriendValidation inviteFriendValidation;
	private final IUserApi userApi;
	private final UserSessionRepository userSessionRepository;

	public UserAccountController(InviteFriendValidation inviteFriendValidation, IUserApi userApi,
			UserSessionRepository userSessionRepository) {
		this.inviteFriendValidation = inviteFriendValidation;
		this.userApi = userApi;
		this.userSessionRepository = userSessionRepository;
	}

	@RequestMapping(value = "/ProcessByEmail", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> inviteFriendsByEmail(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody InviteFriendsDTO friend, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(friend, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				InviteFriendsError error = new InviteFriendsError();
				String sessionId = friend.getSessionId();
				error = inviteFriendValidation.checkErrorByEmail(friend);
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (error.isValid()) {
					if (userSession != null) {
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						String authority = user.getAuthority();
						if (authority.contains(Authorities.USER) && authority.contains(Authorities.AUTHENTICATED)) {
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Invitation Sent to " + friend.getEmail());
							result.setDetails("Invitation Sent to " + friend.getEmail());
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Permission Not Granted");
							result.setDetails("Permission Not Granted");
						}
					} else {
						result.setStatus(ResponseStatus.INVALID_SESSION);
						result.setMessage("Invalid Session");
						result.setDetails("Invalid Session");
					}
				} else {
					result.setStatus(ResponseStatus.BAD_REQUEST);
					result.setMessage(""+error);
					result.setDetails(error);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Permission Not Granted");
				result.setDetails("Permission Not Granted");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/ProcessByMobile", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> inviteFriendsByMobile(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@ModelAttribute InviteFriendsDTO friend, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(friend, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				InviteFriendsError error = new InviteFriendsError();
				String sessionId = friend.getSessionId();
				error = inviteFriendValidation.checkErrorByMobile(friend);
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (error.isValid()) {
					if (userSession != null) {
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						String authority = user.getAuthority();
						if (authority.contains(Authorities.USER) && authority.contains(Authorities.AUTHENTICATED)) {
							if (user != null) {
								result.setStatus(ResponseStatus.SUCCESS);
								result.setMessage("User already exists");
								result.setDetails("User already exists");
							}
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Permission Not Granted");
							result.setDetails("Permission Not Granted");
						}
					} else {
						result.setStatus(ResponseStatus.INVALID_SESSION);
						result.setMessage("Invalid Session");
						result.setDetails("Invalid Session");
					}
				} else {
					result.setStatus(ResponseStatus.BAD_REQUEST);
					result.setMessage(""+error);
					result.setDetails(error);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Permission Not Granted");
				result.setDetails("Permission Not Granted");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

}
