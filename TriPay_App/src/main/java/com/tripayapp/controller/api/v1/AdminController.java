package com.tripayapp.controller.api.v1;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tripayapp.api.*;
import com.tripayapp.api.IMerchantApi;
import com.tripayapp.entity.*;
import com.tripayapp.model.*;
import com.tripayapp.repositories.PGDetailsRepository;
import com.tripayapp.repositories.PQServiceRepository;
import com.tripayapp.util.ConvertUtil;
import com.tripayapp.util.StartupUtil;
import com.thirdparty.api.*;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tripayapp.model.error.ChangePasswordError;
import com.tripayapp.model.error.PromoCodeError;
import com.tripayapp.model.error.RegisterError;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.PromoCodeValidation;
import com.tripayapp.validation.RegisterValidation;

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}")
public class AdminController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
	private final PromoCodeValidation promoCodeValidation;
	private final IAdminApi adminApi;
	private final ISendMoneyApi sendMoneyApi;
	private final PQServiceRepository pqServiceRepository;
	public AdminController(IUserApi userApi, IMerchantApi merchantApi, UserSessionRepository userSessionRepository,
			PersistingSessionRegistry persistingSessionRegistry, ISessionApi sessionApi, ITransactionApi transactionApi,
			IMessageLogApi messageLogApi, IEmailLogApi emailLogApi, RegisterValidation registerValidation,
			IPromoCodeApi promoCodeApi, PromoCodeValidation promoCodeValidation,IAdminApi adminApi,ISendMoneyApi sendMoneyApi,PQServiceRepository pqServiceRepository) {
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
		this.promoCodeValidation = promoCodeValidation;
		this.adminApi = adminApi;
		this.sendMoneyApi = sendMoneyApi;
		this.pqServiceRepository = pqServiceRepository;
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

	@RequestMapping(method = RequestMethod.POST, value = "/GetValues", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> updateProfile(@PathVariable(value = "role") String role,
											  @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
											  @RequestBody SessionDTO dto, @RequestHeader("hash") String hash, HttpServletRequest request,
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
							try {
								result.setStatus(ResponseStatus.SUCCESS);
								result.setMessage("values");
								result.setDetails(userApi.getAdminLoginValues());
								return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
							} catch (Exception e) {
								e.printStackTrace();
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Failed, Please try again later.");
								result.setDetails("Failed, Please try again later.");
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
						userApi.updateUserAuthority(Authorities.USER + "," + Authorities.LOCKED, blockUser.getId());
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

	@RequestMapping(method = RequestMethod.POST, value = "/RefundAmount", produces = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> refundMoneyFromUser(@PathVariable(value = "role") String role,
										  @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
										  @RequestBody RefundDTO dto, @RequestHeader(value = "hash", required = true) String hash,
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
						userApi.blockUser(dto.getUsername());
						User blockUser = userApi.findByUserName(dto.getUsername());
						if(blockUser != null) {
							result = sendMoneyApi.refundMoneyToAccount(dto, blockUser);
						}else {
							result.setStatus(ResponseStatus.FAILURE);
							result.setMessage("User Not Found");
						}
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("Unauthorized User");
						result.setDetails("Unauthorized User");
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Invalid Session");
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

	@RequestMapping(method = RequestMethod.POST, value = "/LMTransactions", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getLoadMoneyTransactions(@PathVariable(value = "role") String role,
											@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
											@RequestBody SessionDTO dto, @RequestHeader(value = "hash", required = true) String hash,
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
						List<PQTransaction> transactionList = transactionApi.getLoadMoneyTransactions(Status.Success);
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Transaction List");
						result.setDetails(transactionList);
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("Un-Authorized User");
						result.setDetails("Un-Authorized User");
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Invalid Session");
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

	@RequestMapping(method = RequestMethod.POST, value = "/PCTransactions", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getRedeemTransactions(@PathVariable(value = "role") String role,
														 @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
														 @RequestBody PagingDTO dto, @RequestHeader(value = "hash", required = true) String hash,
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
						PQService service = pqServiceRepository.findServiceByCode("PPS");
						List<PQTransaction> transactionList = transactionApi.transactionListByServiceAndDebit(service,false);
						List<User> userList = userApi.getAllUsers();
						List<MTransactionResponseDTO> minimizedList = ConvertUtil.getMerchantTransactions(transactionList,userList);
						Page<MTransactionResponseDTO> promoCodeTransactions = ConvertUtil.convertFromList(minimizedList,dto);
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Transaction List");
						result.setDetails(promoCodeTransactions);
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("Un-Authorized User");
						result.setDetails("Un-Authorized User");
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Invalid Session");
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
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody MRegisterDTO dto, @RequestHeader(value = "hash", required = true) String hash,
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
						String email = dto.getEmail();
						User merchant = userApi.findByUserName(email);
						if(merchant == null) {
							PGDetails pgDetails = userApi.findMerchantByMobile(dto.getContactNo());
							if(pgDetails == null) {
								result = merchantApi.addMerchant(dto);
								result.setStatus(ResponseStatus.SUCCESS);
								result.setMessage("Merchant added successfully");
								return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
							}else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Mobile already exists");
								return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
							}
						}else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Merchant Already exists with this EMAIL ID");
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
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String languagemys, @RequestBody PagingDTO dto,
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
						String type = "ALL";
						List<MerchantDTO> merchants = merchantApi.getAll(type);
						result.setMessage("Merchant List");
						result.setStatus(ResponseStatus.SUCCESS);
						result.setDetails(merchants);
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
						PromoCode isCodeExist = promoCodeApi.checkPromoCodeValid(dto.getPromoCode());
							if (isCodeExist == null) {
								boolean isValidCode = promoCodeApi.checkPromoCodeLength(dto.getPromoCode());
								if (isValidCode) {
									promoCodeApi.addPromocode(dto);
									result.setMessage("PromoCode added successfully.");
									result.setStatus(ResponseStatus.SUCCESS);
									result.setDetails(null);
									return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
								}
								result.setMessage("Please Enter 6 digit Promo Code");
								result.setStatus(ResponseStatus.FAILURE);
								result.setDetails(null);
								return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
							}
							result.setMessage("Promo Code Already Exist");
							result.setStatus(ResponseStatus.FAILURE);
							result.setDetails(null);
							return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);

					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("Unauthorized user.");
						result.setDetails("Unauthorized user.");
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Session invalid.");
					result.setDetails("Session invalid.");
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
	ResponseEntity<ResponseDTO> listPromoCodes(@PathVariable(value = "role") String role,
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



	@RequestMapping(method = RequestMethod.POST, value = "/BankTransferList", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getBankTransferRequests(@PathVariable(value = "role") String role,
										   @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
										   @RequestBody SessionDTO dto, @RequestHeader(value = "hash", required = true) String hash,
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
						List<BankTransfer> bankTransfers = adminApi.getAllTransferReports();
						List<BankTransferDTO> bankTransferDTOs = new ArrayList<>();
						User bank = userApi.findByUserName("bank@vpayqwik.com");
						String accountNumber = String.valueOf(bank.getAccountDetail().getAccountNumber());
						for(BankTransfer bt: bankTransfers){
							BankTransferDTO bdto = new BankTransferDTO();
							bdto.setTransactionDate(dateFormat.format(bt.getCreated()));
							bdto.setName(bt.getSender().getUserDetail().getFirstName());
							bdto.setEmail(bt.getSender().getUserDetail().getEmail());
							bdto.setMobileNumber(bt.getSender().getUsername());
							bdto.setAmount(""+bt.getAmount());
							BankDetails bankDetails = bt.getBankDetails();
							if(bankDetails != null) {
								bdto.setBankName(bankDetails.getBank().getName());
								bdto.setIfscCode(bankDetails.getIfscCode());
							}
							bdto.setBeneficiaryAccountName(bt.getBeneficiaryName());
							bdto.setBeneficiaryAccountNumber(""+bt.getBeneficiaryAccountNumber());
							bdto.setVirtualAccount(""+bt.getSender().getAccountDetail().getAccountNumber());
							bdto.setTransactionID(bt.getTransactionRefNo());
							bdto.setBankVirtualAccount(accountNumber);
							bdto.setStatus(String.valueOf(Status.Success));
							PQTransaction temp = adminApi.getTransactionByRefNo(bt.getTransactionRefNo());
							if(temp != null){
								bdto.setStatus(temp.getStatus().getValue());
								bdto.setTransactionDate(dateFormat.format(temp.getCreated()));
							}
							bankTransferDTOs.add(bdto);
						}



						result.setMessage("PromoCode list.");
						result.setStatus(ResponseStatus.SUCCESS);
						result.setDetails(bankTransferDTOs);
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

	@RequestMapping(method = RequestMethod.POST, value = "/UpdateBankTransferStatus", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getBankTransferRequests(@PathVariable(value = "role") String role,
														@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
														@RequestBody CallBackDTO dto, @RequestHeader(value = "hash", required = true) String hash,
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
						String transactionRefNo = dto.getTransactionRefNo();
						String message = "";
						if(dto.isSuccess()){
							sendMoneyApi.sendMoneyBankSuccess(transactionRefNo);
							message = "Bank Transfer Successful";
						}else{
							sendMoneyApi.sendMoneyBankFailed(transactionRefNo);
							message = "Bank Transfer Failed";
						}
						result.setMessage(message);
						result.setStatus(ResponseStatus.SUCCESS);
						result.setDetails("");
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

	
	@RequestMapping(method = RequestMethod.POST, value = "/MBankTransferList", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> merchantBankTransferRequests(@PathVariable(value = "role") String role,
										   @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
										   @RequestBody SessionDTO dto, @RequestHeader(value = "hash", required = true) String hash,
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
						List<MBankTransfer> mBankTransfers = adminApi.getAllMBankTransferReports();
						List<BankTransferDTO> bankTransferDTOs = new ArrayList<>();
						User bank = userApi.findByUserName("mbank@vpayqwik.com");
						String accountNumber = String.valueOf(bank.getAccountDetail().getAccountNumber());
						for(MBankTransfer bt: mBankTransfers){
							BankTransferDTO bdto = new BankTransferDTO();
							bdto.setTransactionDate(dateFormat.format(bt.getCreated()));
							bdto.setName(bt.getSender().getUserDetail().getFirstName());
							bdto.setEmail(bt.getSender().getUserDetail().getEmail());
							bdto.setMobileNumber(bt.getSender().getUsername());
							bdto.setAmount(""+bt.getAmount());
							BankDetails bankDetails = bt.getBankDetails();
							if(bankDetails != null) {
								bdto.setBankName(bankDetails.getBank().getName());
								bdto.setIfscCode(bankDetails.getIfscCode());
							}
							bdto.setBeneficiaryAccountName(bt.getBeneficiaryName());
							bdto.setBeneficiaryAccountNumber(""+bt.getBeneficiaryAccountNumber());
							bdto.setVirtualAccount(""+bt.getSender().getAccountDetail().getAccountNumber());
							bdto.setTransactionID(bt.getTransactionRefNo());
							bdto.setBankVirtualAccount(accountNumber);
							bdto.setStatus(String.valueOf(Status.Success));
							PQTransaction temp = adminApi.getTransactionByRefNo(bt.getTransactionRefNo());
							if(temp != null){
								bdto.setStatus(temp.getStatus().getValue());
								bdto.setTransactionDate(dateFormat.format(temp.getCreated()));
							}
							bankTransferDTOs.add(bdto);
						}
						result.setMessage("Merchant NEFT Transaction list.");
						result.setStatus(ResponseStatus.SUCCESS);
						result.setDetails(bankTransferDTOs);
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
