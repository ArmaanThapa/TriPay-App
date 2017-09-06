package com.tripayapp.controller.api.v2;

import com.tripayapp.api.*;
import com.tripayapp.entity.*;
import com.tripayapp.model.*;
import com.tripayapp.model.error.ChangePasswordError;
import com.tripayapp.model.error.RegisterError;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.RegisterValidation;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/Api/v2/{role}/{device}/{language}")
public class AdminController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final IUserApi userApi;
	private final IMerchantApi merchantApi;
	private final UserSessionRepository userSessionRepository;
	private final PersistingSessionRegistry persistingSessionRegistry;
	private final ISessionApi sessionApi;
	private final ITransactionApi transactionApi;
	private final IMessageLogApi messageLogApi;
	private final IEmailLogApi emailLogApi;
	private final RegisterValidation registerValidation;
	private final IPromoCodeApi promoCodeApi;

	public AdminController(IUserApi userApi, IMerchantApi merchantApi, UserSessionRepository userSessionRepository,
			PersistingSessionRegistry persistingSessionRegistry, ISessionApi sessionApi, ITransactionApi transactionApi,
			IMessageLogApi messageLogApi, IEmailLogApi emailLogApi, RegisterValidation registerValidation,
			IPromoCodeApi promoCodeApi) {
		this.userApi = userApi;
		this.merchantApi = merchantApi;
		this.userSessionRepository = userSessionRepository;
		this.persistingSessionRegistry = persistingSessionRegistry;
		this.sessionApi = sessionApi;
		this.transactionApi = transactionApi;
		this.messageLogApi = messageLogApi;
		this.emailLogApi = emailLogApi;
		this.registerValidation = registerValidation;
		this.promoCodeApi = promoCodeApi;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/Update/Profile", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> updateProfile(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody RegisterDTO dto, @RequestHeader("hash") String hash, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);

		if (isValidHash) {
			if (role.equalsIgnoreCase("Admin")) {
				String sessionId = dto.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						RegisterError error = new RegisterError();
						error = registerValidation.validateEditUser(dto);
						if (error.isValid()) {
							try {
								userApi.editUser(dto);
								result.setStatus(ResponseStatus.SUCCESS);
								result.setMessage("Profile updated successfully.");
								result.setDetails("Profile updated successfully.");
								return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
							} catch (Exception e) {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Failed, Please try again later.");
								result.setDetails("Failed, Please try again later.");
								return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
							}
						} else {
							result.setStatus(ResponseStatus.BAD_REQUEST);
							result.setMessage("Failed, Please try again later.");
							result.setDetails(error);
							return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
						}

					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("Failed, Unauthorized user.");
						result.setDetails("Failed, Unauthorized user.");
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Please, login and try again.");
					result.setDetails("Please, login and try again.");
					return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
				result.setMessage("Failed, Unauthorized user.");
				result.setDetails("Failed, Unauthorized user.");
				return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);

			}
		} else {
			result.setStatus(ResponseStatus.INVALID_HASH);
			result.setMessage("Failed, Please try again later.");
			return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/ChangePassword", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> changePassword(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody ChangePasswordDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);

		if (isValidHash) {
			if (role.equalsIgnoreCase("admin")) {
				String sessionId = dto.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						ChangePasswordError error = new ChangePasswordError();
						error = registerValidation.validateChangePassword(dto);
						if (error.isValid()) {
							try {
								userApi.changePassword(dto, userSession.getUser().getId());
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Change Password");
								result.setDetails("Password Has Been Changed Successfully");
								return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
							} catch (Exception e) {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Change Password");
								result.setDetails(e.getMessage());
								return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
							}
						} else {
							result.setStatus(ResponseStatus.BAD_REQUEST);
							result.setMessage("Change Password");
							result.setDetails(error);
							return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
						}

					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("Change Password");
						result.setDetails("Permission Not Granted");
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Change Password");
					result.setDetails("Invalid Session");
					return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Change Password");
				result.setDetails("Permission Not Granted");
				return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);

			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
			return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/BlockUser", produces = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> blockUser(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody UserRequestDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("Admin")) {
				String sessionId = dto.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						User blockUser = userApi.findByUserName(dto.getUsername());
						userApi.updateUserAuthority(Authorities.USER + "," + Authorities.BLOCKED, blockUser.getId());
						sessionApi.clearAllSessionForUser(blockUser);
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("User blocked and session cleared");
						result.setDetails("Block user success");
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("Block user");
						result.setDetails("Block user");
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Block User");
					result.setDetails("Invalid Session");
					return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
				}
			}
			result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
			result.setMessage("Unauthorized user");
			result.setDetails("Permission Not Granted");
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/UnblockUser", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> unblockUser(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody UserRequestDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("Admin")) {
				String sessionId = dto.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						User blockUser = userApi.findByUserName(dto.getUsername());
						userApi.updateUserAuthority(Authorities.USER + "," + Authorities.AUTHENTICATED,
								blockUser.getId());
						sessionApi.clearAllSessionForUser(blockUser);
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("User unblocked and session cleared");
						result.setDetails("Unblock user success");
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("Unblock user");
						result.setDetails("Unblock user");
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Unblock User");
					result.setDetails("Invalid Session");
					return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
				}
			}
			result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
			result.setMessage("Unauthorized user");
			result.setDetails("Permission Not Granted");
			return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
			return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/Report/{reportType}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getTransactionReport(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@PathVariable(value = "reportType") String report, @RequestBody GetTransactionDTO dto,
			@RequestHeader(value = "hash", required = true) String hash, HttpServletRequest request,
			HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);

		if (role.equalsIgnoreCase("Admin")) {
			String sessionId = dto.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (userSession != null) {
				UserDTO user = userApi.getUserById(userSession.getUser().getId());
				if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
						&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
					persistingSessionRegistry.refreshLastRequest(sessionId);
					if (report.equalsIgnoreCase("Transaction")) {
						List<PQTransaction> transactions = null;
						String username = dto.getUsername();
						if (username != null && username.length() != 0) {
							User userTransaction = userApi.findByUserName(dto.getUsername());
							transactions = transactionApi.getDailyTransactionBetweenForAccount(dto.getStart(),
									dto.getEnd(), userTransaction.getAccountDetail());
							result.setMessage("User transaction report.");
						} else {
							transactions = transactionApi.getDailyTransactionBetweeen(dto.getStart(), dto.getEnd());
							result.setMessage("Transaction successful.");
						}
						result.setStatus(ResponseStatus.SUCCESS);
						result.setDetails(transactions);
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					} else if (report.equalsIgnoreCase("Email")) {
						List<EmailLog> emailLogs = null;
						emailLogs = emailLogApi.getDailyEmailLogBetweeen(dto.getStart(), dto.getEnd());
						result.setMessage("Email Log successful.");
						result.setStatus(ResponseStatus.SUCCESS);
						result.setDetails(emailLogs);
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					} else if (report.equalsIgnoreCase("SMS")) {
						List<MessageLog> messageLogs = null;
						messageLogs = messageLogApi.getDailyMessageLogBetweeen(dto.getStart(), dto.getEnd());
						result.setMessage("Message log successful.");
						result.setStatus(ResponseStatus.SUCCESS);
						result.setDetails(messageLogs);
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					}
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

	}

	@RequestMapping(method = RequestMethod.POST, value = "/Merchant/Save", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> saveMerchant(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language, @RequestBody RegisterDTO dto,
			@RequestHeader(value = "hash", required = true) String hash, HttpServletRequest request,
			HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("Admin")) {
				String sessionId = dto.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
//						merchantApi.addMerchant(dto);
						result.setMessage("Merchant Added Successfully");
						result.setStatus(ResponseStatus.SUCCESS);
						result.setDetails(null);
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
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

	@RequestMapping(method = RequestMethod.POST, value = "/Merchant/All", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getMerchant(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@PathVariable(value = "reportType") String report, @RequestBody PagingDTO dto,
			@RequestHeader(value = "hash", required = true) String hash, HttpServletRequest request,
			HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("Admin")) {
				String sessionId = dto.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
//						List<UserDTO> merchants = merchantApi.getAll();
						result.setMessage("Merchant added successfully.");
						result.setStatus(ResponseStatus.SUCCESS);
//						result.setDetails(merchants);
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
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

	@RequestMapping(method = RequestMethod.POST, value = "/PromoCode/Save", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> savePromoCode(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody PromoCodeDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("Admin")) {
				String sessionId = dto.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {

					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						promoCodeApi.addPromocode(dto);
						result.setMessage("PromoCode added successfully.");
						result.setStatus(ResponseStatus.SUCCESS);
						result.setDetails(null);
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
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

	@RequestMapping(method = RequestMethod.POST, value = "/PromoCode/List", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> lPromoCode(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SessionDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		List<PromoCodeDTO> list = new ArrayList<PromoCodeDTO>();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("Admin")) {
				String sessionId = dto.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					promoCodeApi.getAll();
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						list = promoCodeApi.getAll();
						result.setMessage("PromoCode list.");
						result.setStatus(ResponseStatus.SUCCESS);
						result.setDetails(list);
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
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
