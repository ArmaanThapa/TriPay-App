package com.tripayapp.controller.api.v2;

import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.CouponDTO;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.error.CouponError;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.RedeemCouponValidation;
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
@RequestMapping("/Api/v2/{role}/{device}/{language}/Coupon")
public class CouponController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final IUserApi userApi;
	private final RedeemCouponValidation redeemCouponValidation;
	private final UserSessionRepository userSessionRepository;

	public CouponController(IUserApi userApi, RedeemCouponValidation redeemCouponValidation,
			UserSessionRepository userSessionRepository) {
		this.userApi = userApi;
		this.redeemCouponValidation = redeemCouponValidation;
		this.userSessionRepository = userSessionRepository;
	}

	@RequestMapping(value = "/Redeem", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processCoupon(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody CouponDTO coupon, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		String sessionId = coupon.getSessionId();
		boolean isValidHash = SecurityUtil.isHashMatches(coupon, hash);
		ResponseDTO result = new ResponseDTO();
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				CouponError error = new CouponError();
				error = redeemCouponValidation.isValidCoupon(coupon);
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (error.isValid()) {
					if (userSession != null) {
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("User | Redeem Coupon");
							result.setDetails("Coupon Number " + coupon.getCouponNumber() + "is Added");
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("User | Redeem Coupon");
							result.setDetails("Permission Not Granted");
						}
					}
				} else {
					result.setStatus(ResponseStatus.BAD_REQUEST);
					result.setMessage("User | Redeem Coupon");
					result.setDetails(error);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("User | Redeem Coupon");
				result.setDetails("Permission Not Granted");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

}
