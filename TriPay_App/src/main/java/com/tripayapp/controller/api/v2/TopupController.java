package com.tripayapp.controller.api.v2;

import com.instantpay.model.response.TransactionResponse;
import com.tripayapp.api.ITopupAndBillPaymentApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.MobileTopupDTO;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.error.MobileTopupError;
import com.tripayapp.model.error.TransactionError;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.PQServiceRepository;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.MobileTopupValidation;
import com.tripayapp.validation.TransactionValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/Api/v2/{role}/{device}/{language}/MobileTopup")
public class TopupController implements MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MessageSource messageSource;

	private final IUserApi userApi;
	private final ITopupAndBillPaymentApi topupAndBillPaymentApi;
	private final MobileTopupValidation mobileTopupValidation;
	private final TransactionValidation transactionValidation;
	private final PersistingSessionRegistry persistingSessionRegistry;
	private final UserSessionRepository userSessionRepository;
	private final PQServiceRepository pqServiceRepository;

	public TopupController(IUserApi userApi, ITopupAndBillPaymentApi topupAndBillPaymentApi,
			MobileTopupValidation mobileTopupValidation, TransactionValidation transactionValidation,
			PersistingSessionRegistry persistingSessionRegistry, UserSessionRepository userSessionRepository,
			PQServiceRepository pqServiceRepository) {
		this.userApi = userApi;
		this.topupAndBillPaymentApi = topupAndBillPaymentApi;
		this.mobileTopupValidation = mobileTopupValidation;
		this.transactionValidation = transactionValidation;
		this.persistingSessionRegistry = persistingSessionRegistry;
		this.userSessionRepository = userSessionRepository;
		this.pqServiceRepository = pqServiceRepository;
		
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@RequestMapping(value = "/ProcessPrepaid", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processTopup(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody MobileTopupDTO topup, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(topup, hash);

		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				MobileTopupError error = new MobileTopupError();
				error = mobileTopupValidation.checkError(topup);
				if (error.isValid()) {
					String sessionId = topup.getSessionId();
					UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
					if (userSession != null) {
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							PQService service = pqServiceRepository
									.findServiceByCode(topup.getServiceProvider());
////							TransactionError transactionError = transactionValidation
////									.validateBillPayment(topup.getAmount(), user.getUsername(), service);
//							if (transactionError.isValid()) {
//								TransactionResponse ipayResponse = topupAndBillPaymentApi.prepaidTopup(topup,
//										user.getUsername(), service);
//								String msg = (ipayResponse.isSuccess() ? ipayResponse.getTransaction().getResMsg()
//										: ipayResponse.getValidation().getIpayErrorDesc());
//								result.setStatus((ipayResponse.isSuccess() ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE));
//								result.setMessage(msg);
//								result.setDetails(msg);
//								User u = userApi.findByUserName(user.getUsername());
//								result.setBalance(u.getAccountDetail().getBalance());
//							} else {
//								result.setStatus(ResponseStatus.FAILURE);
//								result.setMessage("Prepaid topup failed. Please try again later.");
//								result.setDetails(transactionError.getMessage());
//							}
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Failed, Unauthorized user.");
							result.setDetails("Failed, Unauthorized user.");
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

	@RequestMapping(value = "/ProcessPostpaid", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processPostpaidTopup(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody MobileTopupDTO topup, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(topup, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				MobileTopupError error = new MobileTopupError();
				error = mobileTopupValidation.checkError(topup);
				if (error.isValid()) {
					String sessionId = topup.getSessionId();
					UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
					if (userSession != null) {
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							PQService service = pqServiceRepository
									.findServiceByCode(topup.getServiceProvider());
//							TransactionError transactionError = transactionValidation
//									.validateBillPayment(topup.getAmount(), user.getUsername(), service);
//							if (transactionError.isValid()) {
//								TransactionResponse ipayResponse = topupAndBillPaymentApi.postpaidTopup(topup,
//										user.getUsername(), service);
//								String msg = (ipayResponse.isSuccess() ? ipayResponse.getTransaction().getResMsg()
//										: ipayResponse.getValidation().getIpayErrorDesc());
//								result.setStatus(
//										(ipayResponse.isSuccess() ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE));
//								result.setMessage(msg);
//								result.setDetails(msg);
//								User u = userApi.findByUserName(user.getUsername());
//								result.setBalance(u.getAccountDetail().getBalance());
//							} else {
//								result.setStatus(ResponseStatus.FAILURE);
//								result.setMessage("Postpaid topup failed. Please try again later.");
//								result.setDetails(transactionError.getMessage());
//							}
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Failed, Unauthorized user.");
							result.setDetails("Failed, Unauthorized user.");
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

	@RequestMapping(value = "/ProcessDataCard", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processDataCardTopup(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody MobileTopupDTO topup, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(topup, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				MobileTopupError error = new MobileTopupError();
				error = mobileTopupValidation.checkError(topup);
				if (error.isValid()) {
					String sessionId = topup.getSessionId();
					UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
					if (userSession != null) {
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							PQService service = pqServiceRepository
									.findServiceByCode(topup.getServiceProvider());
//							TransactionError transactionError = transactionValidation
//									.validateBillPayment(topup.getAmount(), user.getUsername(), service);
//							if (transactionError.isValid()) {
//								TransactionResponse ipayResponse = topupAndBillPaymentApi.datacardTopup(topup,
//										user.getUsername(), service);
//								String msg = (ipayResponse.isSuccess() ? ipayResponse.getTransaction().getResMsg()
//										: ipayResponse.getValidation().getIpayErrorDesc());
//								result.setStatus(
//										(ipayResponse.isSuccess() ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE));
//								result.setMessage(msg);
//								result.setDetails(msg);
//								User u = userApi.findByUserName(user.getUsername());
//								result.setBalance(u.getAccountDetail().getBalance());
							} else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Postpaid topup failed. Please try again later.");
//								result.setDetails(transactionError.getMessage());
							}
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Failed, Unauthorized user.");
							result.setDetails("Failed, Unauthorized user.");
						}
					} else {
						result.setStatus(ResponseStatus.INVALID_SESSION);
						result.setMessage("Please, login and try again.");
						result.setDetails("Please, login and try again.");
					}
				} else {
					result.setStatus(ResponseStatus.BAD_REQUEST);
					result.setMessage("Failed, invalid request.");
//					result.setDetails(error);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
				result.setMessage("Failed, Unauthorized user.");
				result.setDetails("Failed, Unauthorized user.");
			}
//		} else {
//			result.setStatus(ResponseStatus.INVALID_HASH);
//			result.setMessage("Failed, Please try again later.");
//		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

}
