package com.tripayapp.controller.api.v2;

import com.tripayapp.api.ISendMoneyApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.SendMoneyBankDTO;
import com.tripayapp.model.SendMoneyBankSettleDTO;
import com.tripayapp.model.SendMoneyMobileDTO;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.error.SendMoneyBankError;
import com.tripayapp.model.error.SendMoneyMobileError;
import com.tripayapp.model.error.TransactionError;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.PQServiceRepository;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.SendMoneyValidation;
import com.tripayapp.validation.TransactionValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/Api/v2/{role}/{device}/{language}/SendMoney")
public class SendMoneyController implements MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MessageSource messageSource;

	private final ISendMoneyApi sendMoneyApi;
	private final IUserApi userApi;
	private final SendMoneyValidation sendMoneyValidation;
	private final TransactionValidation transactionValidation;
	private final UserSessionRepository userSessionRepository;
	private final PersistingSessionRegistry persistingSessionRegistry;
	private final PQServiceRepository pqServiceRepository;

	public SendMoneyController(ISendMoneyApi sendMoneyApi, IUserApi userApi, SendMoneyValidation sendMoneyValidation,
			TransactionValidation transactionValidation, UserSessionRepository userSessionRepository,
			PersistingSessionRegistry persistingSessionRegistry, PQServiceRepository pqServiceRepository) {
		this.sendMoneyApi = sendMoneyApi;
		this.userApi = userApi;
		this.sendMoneyValidation = sendMoneyValidation;
		this.transactionValidation = transactionValidation;
		this.userSessionRepository = userSessionRepository;
		this.persistingSessionRegistry = persistingSessionRegistry;
		this.pqServiceRepository = pqServiceRepository;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@RequestMapping(value = "/Mobile", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processMobile(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SendMoneyMobileDTO mobile, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {

		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(mobile, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				SendMoneyMobileError error = new SendMoneyMobileError();
				String sessionId = mobile.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					String serviceCode = sendMoneyApi.prepareSendMoney(mobile.getMobileNumber());
					error = sendMoneyValidation.checkMobileError(mobile, user.getUsername(), serviceCode);
					if (error.isValid()) {
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							PQService service = pqServiceRepository.findServiceByCode(serviceCode);
							TransactionError transactionError = transactionValidation.validateP2PTransaction(
									mobile.getAmount(), user.getUsername(), mobile.getMobileNumber(), service);
							if (transactionError.isValid()) {
								sendMoneyApi.sendMoneyMobile(mobile, user.getUsername(), service);
								result.setStatus(ResponseStatus.SUCCESS);
								result.setMessage(
										"Sent money to mobile successful. Money Sent to " + mobile.getMobileNumber());
								result.setDetails("Money Sent to " + mobile.getMobileNumber());
								User u = userApi.findByUserName(user.getUsername());
								result.setBalance(u.getAccountDetail().getBalance());
							} else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Please try again later.");
								result.setDetails(transactionError.getMessage());
							}
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Failed, Unauthorized user.");
							result.setDetails("Failed, Unauthorized user.");
						}
					} else {
						result.setStatus(ResponseStatus.BAD_REQUEST);
						result.setMessage("Failed, invalid request.");
						result.setDetails(error);
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Please, login and try again.");
					result.setDetails("Please, login and try again.");
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

	@RequestMapping(value = "/Bank/Initiate", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> initiateBank(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SendMoneyBankDTO bank, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(device, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				SendMoneyBankError error = new SendMoneyBankError();
				error = sendMoneyValidation.checkBankError(bank, "SMB");
				if (error.isValid()) {
					String sessionId = bank.getSessionId();
					UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
					if (userSession != null) {
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							PQService service = pqServiceRepository.findServiceByCode("SMB");
							TransactionError transactionError = transactionValidation
									.validateMerchantTransaction(bank.getAmount(), user.getUsername(), service);
							if (transactionError.isValid()) {
								sendMoneyApi.sendMoneyBankInitiate(bank, user.getUsername(), service);
								result.setStatus(ResponseStatus.SUCCESS);
								result.setMessage("Your request is initiated for " + bank.getAccountNumber()+"  where account holder is" + bank.getAccountName());
								result.setDetails("");
								User u = userApi.findByUserName(user.getUsername());
								if (u != null) {
									result.setBalance(u.getAccountDetail().getBalance());
								}
							} else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Error Occurred In Transaction Validation");
								result.setDetails(transactionError);
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
					result.setMessage("Error Occurred while Processing Request");
					result.setDetails(error);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("User | Send Money Bank");
				result.setDetails("Permission Not Granted");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/Bank/Success", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> successBank(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SendMoneyBankSettleDTO bank, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(device, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				String sessionId = bank.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						sendMoneyApi.sendMoneyBankSuccess(bank.getTransactionRefNo());
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("User | Send Money Bank");
						result.setDetails("Bank transfer success. Transaction Reference Number " + bank.getTransactionRefNo());
						User u = userApi.findByUserName(user.getUsername());
						if (u != null) {
							result.setBalance(u.getAccountDetail().getBalance());
						}

					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("User | Send Money Bank");
						result.setDetails("Permission Not Granted");
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("User | Send Money Bank");
					result.setDetails("Invalid Session");
				}

			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("User | Send Money Bank");
				result.setDetails("Permission Not Granted");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/Bank/Failed", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> failedBank(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SendMoneyBankSettleDTO bank, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(device, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				String sessionId = bank.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						sendMoneyApi.sendMoneyBankFailed(bank.getTransactionRefNo());
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("User | Send Money Bank");
						result.setDetails(
								"Bank transfer failed. Transaction Reference Number " + bank.getTransactionRefNo());
						User u = userApi.findByUserName(user.getUsername());
						if (u != null) {
							result.setBalance(u.getAccountDetail().getBalance());
						}
					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("User | Send Money Bank");
						result.setDetails("Permission Not Granted");
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("User | Send Money Bank");
					result.setDetails("Invalid Session");
				}

			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("User | Send Money Bank");
				result.setDetails("Permission Not Granted");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

}
