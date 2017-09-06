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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.User;
import com.tripayapp.model.ChangePasswordDTO;
import com.tripayapp.model.ForgotPasswordDTO;
import com.tripayapp.model.RegisterDTO;
import com.tripayapp.model.Status;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.UserType;
import com.tripayapp.model.VerifyMobileDTO;
import com.tripayapp.model.error.ChangePasswordError;
import com.tripayapp.model.error.ForgotPasswordError;
import com.tripayapp.model.error.RegisterError;
import com.tripayapp.model.error.VerifyEmailOTPError;
import com.tripayapp.model.error.VerifyMobileOTPError;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.EmailOTPValidation;
import com.tripayapp.validation.MobileOTPValidation;
import com.tripayapp.validation.RegisterValidation;

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}")
public class RegisterController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final IUserApi userApi;
	private final RegisterValidation registerValidation;
	private final MobileOTPValidation mobileOTPValidation;
	private final EmailOTPValidation emailOTPValidation;

	public RegisterController(IUserApi userApi, RegisterValidation registerValidation,
			MobileOTPValidation mobileOTPValidation, EmailOTPValidation emailOTPValidation) {
		super();
		this.userApi = userApi;
		this.registerValidation = registerValidation;
		this.mobileOTPValidation = mobileOTPValidation;
		this.emailOTPValidation = emailOTPValidation;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/Register", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> registerUser(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody RegisterDTO user, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response)
					throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(user, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				RegisterError error = new RegisterError();
				try {
					user.setUsername(user.getContactNo());
					error = registerValidation.validateNormalUser(user);
					if (error.isValid()) {
						user.setUserType(UserType.User);
						userApi.saveUser(user);
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("User Registration Successful and OTP sent to ::" + user.getContactNo()
								+ " and  verification mail sent to ::" + user.getEmail() + " .");
						result.setDetails("User Registration Successful and OTP sent to ::" + user.getContactNo()
								+ " and  verification mail sent to ::" + user.getEmail() + " .");
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					} else {
						result.setStatus(ResponseStatus.BAD_REQUEST);
						result.setMessage("Register User");
						result.setDetails(error);
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					}
				} catch (Exception e) {
					result.setStatus(ResponseStatus.INTERNAL_SERVER_ERROR);
					result.setMessage("Please try again later.");
					result.setDetails(e.getMessage());
					return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
				result.setMessage("Failed, Unauthorized user.");
				result.setDetails("Failed, Unauthorized user.");
			}
		} else {
			result.setStatus(ResponseStatus.INVALID_HASH);
			result.setMessage("Failed, Please try again later.");
			return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);

	}

	@RequestMapping(value = "/ForgotPassword", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> forgotPassword(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody ForgotPasswordDTO forgot, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(forgot, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				String username = forgot.getUsername();
				ForgotPasswordError error = registerValidation.forgotPassword(username);
				if (error.isValid() == true) {
					User u = userApi.findByUserName(username);
					if (u != null) {
						if (u.getAuthority().contains(Authorities.AUTHENTICATED)) {
							if (u.getMobileStatus() == Status.Active) {
								userApi.changePasswordRequest(u);
								result.setStatus(ResponseStatus.SUCCESS);
								result.setMessage("Please, Check your SMS for OTP Code to change your password.");
								result.setDetails("Please, Check your SMS for OTP Code to change your password.");
							} else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Please try again later.");
								result.setDetails("Please try again later.");
							}
						} else {
							result.setStatus(ResponseStatus.FAILURE);
							result.setMessage("Your account is blocked! Please try after 24 hrs.");
							result.setDetails("Your account is blocked! Please try after 24 hrs.");
						}
					} else {
						result.setStatus(ResponseStatus.FAILURE);
						result.setMessage("Please try again later.");
						result.setDetails("Please try again later.");

					}
				} else {
					result.setStatus(ResponseStatus.BAD_REQUEST);
					result.setMessage("Failed, invalid request.");
					result.setDetails(error);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
				result.setMessage("Failed, Unauthorized user.");
				result.setDetails("Failed, Unauthorized user.");
			}
		} else {
			result.setStatus(ResponseStatus.INVALID_HASH);
			result.setMessage("Failed, Please try again later.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/Activate/Mobile", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> verifyUserMobile(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody VerifyMobileDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);

		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				if (verifyUserMobileToken(dto.getKey(), dto.getMobileNumber())) {
					result.setStatus(ResponseStatus.SUCCESS);
					result.setMessage("Your Mobile is Successfully Verified");
					result.setDetails("Your Mobile is Successfully Verified");
				} else {
					result.setStatus(ResponseStatus.BAD_REQUEST);
					result.setMessage("Invalid Activation Key");
					result.setDetails("Invalid Activation Key");
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Not a valid User");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}

		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/Resend/Mobile/OTP", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> generateNewOtp(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody VerifyMobileDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				String username = dto.getMobileNumber();
				VerifyMobileOTPError error = new VerifyMobileOTPError();
				error = mobileOTPValidation.validateResendOTP(username);
				if (error.isValid()) {
					userApi.resendMobileToken(username);
					result.setStatus(ResponseStatus.SUCCESS);
					result.setMessage("New OTP sent on " + username);
					result.setDetails("New OTP sent on " + username);
				} else {
					result.setStatus(ResponseStatus.FAILURE);
					result.setMessage("Resend OTP");
					result.setDetails(error);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Not a valid user");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/Change/{key}", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> verifyPasswordPost(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@PathVariable("key") String key, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(key, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				UserDTO dto = userApi.checkPasswordToken(key);
				if (dto != null) {
					result.setStatus(ResponseStatus.SUCCESS);
					result.setMessage("Verify Password");
					result.setDetails("User ::" + dto.getUsername() + " key::" + key);
				} else {
					result.setStatus(ResponseStatus.BAD_REQUEST);
					result.setMessage("Verify Password");
					result.setDetails("Invalid Verification Key");
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Not a valid user");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/RenewPassword", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> renewPassword(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody ChangePasswordDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				ChangePasswordError error = new ChangePasswordError();

				error = registerValidation.renewPasswordValidation(dto);
				if (error.isValid()) {
					userApi.renewPassword(dto);
					result.setStatus(ResponseStatus.SUCCESS);
					result.setMessage("Password Changed Successfully");
					result.setDetails("Password Changed Successfully");
				} else {
					result.setStatus(ResponseStatus.BAD_REQUEST);
					result.setMessage("Renew Password");
					result.setDetails(error);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Not a valid user");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/Activate/Email/{key}", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> verifyUserEmailPost(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@PathVariable("key") String key, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(key, hash);
		if (isValidHash) {

			if (role.equalsIgnoreCase("User")) {
				VerifyEmailOTPError error = new VerifyEmailOTPError();
				error = emailOTPValidation.checkEmailToken(key);
				if (error.isValid()) {
					boolean status = userApi.activateEmail(key);
					if (status) {
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Activate Email");
						result.setDetails("Your E-Mail is Successfully Verified");
					} else {
						result.setStatus(ResponseStatus.FAILURE);
						result.setMessage("Activate Email");
						result.setDetails("Unable to verify email. Please contact customer care.");
					}
				} else {
					result.setStatus(ResponseStatus.BAD_REQUEST);
					result.setMessage("Activate Email");
					result.setDetails(error);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Not a valid user");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	private boolean verifyUserMobileToken(String key, String mobileNumber) {
		if (userApi.checkMobileToken(key, mobileNumber)) {
			return true;
		} else {
			return false;
		}
		
	}
		
}
