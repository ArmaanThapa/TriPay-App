package com.tripayapp.controller.api.v1;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.SessionDTO;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}/Reward")
public class RewardController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final IUserApi userApi;
	private final UserSessionRepository userSessionRepository;
	private final PersistingSessionRegistry persistingSessionRegistry;

	public RewardController(IUserApi userApi, UserSessionRepository userSessionRepository,
			PersistingSessionRegistry persistingSessionRegistry) {
		this.userApi = userApi;
		this.userSessionRepository = userSessionRepository;
		this.persistingSessionRegistry = persistingSessionRegistry;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/Process", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> updateProfile(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SessionDTO session, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(session, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				String sessionId = session.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						try {
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("User | Reward");
							result.setDetails("Rewarded");
							return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
						} catch (Exception e) {
							result.setStatus(ResponseStatus.FAILURE);
							result.setMessage("User | Reward");
							result.setDetails(e.getMessage());
							return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
						}
					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("User | Reward");
						result.setDetails("Permission Not Granted");
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("User | Reward");
					result.setDetails("Invalid Session");
					return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("User | Reward");
				result.setDetails("Permission Not Granted");
				return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
			return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
		}
	}

}
