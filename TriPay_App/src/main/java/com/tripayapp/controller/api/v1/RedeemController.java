package com.tripayapp.controller.api.v1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tripayapp.entity.User;
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

import com.tripayapp.api.IPromoCodeApi;
import com.tripayapp.api.IRedeemCodeApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.PromoCode;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.PromoTransactionDTO;
import com.tripayapp.model.RedeemDTO;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}")
public class RedeemController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final UserSessionRepository userSessionRepository;
	private final IUserApi userApi;
	private final PersistingSessionRegistry persistingSessionRegistry;
	private final IPromoCodeApi promoCodeApi;
	private final IRedeemCodeApi redeemCodeApi;

	public RedeemController(UserSessionRepository userSessionRepository, IUserApi userApi,
			PersistingSessionRegistry persistingSessionRegistry, IPromoCodeApi promoCodeApi,
			IRedeemCodeApi redeemCodeApi) {
		this.userSessionRepository = userSessionRepository;
		this.userApi = userApi;
		this.persistingSessionRegistry = persistingSessionRegistry;
		this.promoCodeApi = promoCodeApi;
		this.redeemCodeApi = redeemCodeApi;

	}

	@RequestMapping(value = "/RedeemProcess", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> redeeem(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody RedeemDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				String sessionId = dto.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						User u = userApi.findByUserName(user.getUsername());
						result = promoCodeApi.process(dto,u);
						//After processing redeem promo code
						double balance = userApi.getWalletBalance(u);
						result.setBalance(balance);
						return new ResponseEntity<ResponseDTO> (result,HttpStatus.OK);
					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("Unauthorized user.");
						result.setDetails(null);
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Session invalid.");
					result.setDetails(null);
					return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
				}
			}
			result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
			result.setMessage("Unauthorized user.");
			result.setDetails(null);
			return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Unable to process request");
			result.setDetails(null);
			return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
		}

	}
}