package com.tripayapp.controller.api.v2;

import com.instantpay.model.response.TransactionResponse;
import com.tripayapp.api.ITopupAndBillPaymentApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.*;
import com.tripayapp.model.error.*;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.PQServiceRepository;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.*;
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
@RequestMapping("/Api/v2/{role}/{device}/{language}/BillPay")
public class BillPaymentController implements MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MessageSource messageSource;

	private final IUserApi userApi;
	private final ITopupAndBillPaymentApi topupAndBillPaymentApi;
	private final DTHBillPaymentValidation dthBillPaymentValidation;
	private final GasBillPaymentValidation gasBillPaymentValidation;
	private final InsuranceBillPaymentValidation insuranceBillPaymentValidation;
	private final ElectricityBillPaymentValidation electricityBillPaymentValidation;
	private final LandlineBillPaymentValidation landlineBillPaymentValidation;
	private final TransactionValidation transactionValidation;
	private final UserSessionRepository userSessionRepository;
	private final PersistingSessionRegistry persistingSessionRegistry;
	private final PQServiceRepository pqServiceRepository;

	public BillPaymentController(IUserApi userApi, ITopupAndBillPaymentApi topupAndBillPaymentApi,
			DTHBillPaymentValidation dthBillPaymentValidation, GasBillPaymentValidation gasBillPaymentValidation,
			InsuranceBillPaymentValidation insuranceBillPaymentValidation,
			ElectricityBillPaymentValidation electricityBillPaymentValidation,
			LandlineBillPaymentValidation landlineBillPaymentValidation, TransactionValidation transactionValidation,
			UserSessionRepository userSessionRepository, PersistingSessionRegistry persistingSessionRegistry,
			PQServiceRepository pqServiceRepository) {
		this.userApi = userApi;
		this.topupAndBillPaymentApi = topupAndBillPaymentApi;
		this.dthBillPaymentValidation = dthBillPaymentValidation;
		this.gasBillPaymentValidation = gasBillPaymentValidation;
		this.insuranceBillPaymentValidation = insuranceBillPaymentValidation;
		this.electricityBillPaymentValidation = electricityBillPaymentValidation;
		this.landlineBillPaymentValidation = landlineBillPaymentValidation;
		this.transactionValidation = transactionValidation;
		this.userSessionRepository = userSessionRepository;
		this.persistingSessionRegistry = persistingSessionRegistry;
		this.pqServiceRepository = pqServiceRepository;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@RequestMapping(value = "/ProcessDTH", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processDTH(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody DTHBillPaymentDTO dth, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dth, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				DTHBillPaymentError error = new DTHBillPaymentError();
				error = dthBillPaymentValidation.checkError(dth);
				if (error.isValid()) {
					String sessionId = dth.getSessionId();
					UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
					if (userSession != null) {
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							PQService service = pqServiceRepository.findServiceByCode(dth.getServiceProvider());
							TransactionError transactionError = transactionValidation
									.validateBillPayment(dth.getAmount(), user.getUsername(), service);
							if (transactionError.isValid()) {
								TransactionResponse ipayResponse = topupAndBillPaymentApi.dthBillPayment(dth,
										user.getUsername(), service);
								String msg = (ipayResponse.isSuccess() ? ipayResponse.getTransaction().getResMsg()
										: ipayResponse.getValidation().getIpayErrorDesc());
								result.setStatus(
										(ipayResponse.isSuccess() ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE));
								result.setMessage(msg);
								result.setDetails(msg);
								User u = userApi.findByUserName(user.getUsername());
								result.setBalance(u.getAccountDetail().getBalance());
							} else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("DTH bill payment failed. Please try again later.");
								result.setDetails(transactionError.getMessage());
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

	@RequestMapping(value = "/ProcessLandline", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processLandline(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody LandlineBillPaymentDTO landline, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(landline, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				LandlineBillPaymentError error = new LandlineBillPaymentError();
				error = landlineBillPaymentValidation.checkError(landline);
				if (error.isValid()) {
					String sessionId = landline.getSessionId();
					UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
					if (userSession != null) {
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							PQService service = pqServiceRepository.findServiceByCode(landline.getServiceProvider());
							TransactionError transactionError = transactionValidation
									.validateBillPayment(landline.getAmount(), user.getUsername(), service);
							if (transactionError.isValid()) {
								TransactionResponse ipayResponse = topupAndBillPaymentApi.landlineBillPayment(landline,
										user.getUsername(), service);
								String msg = (ipayResponse.isSuccess() ? ipayResponse.getTransaction().getResMsg()
										: ipayResponse.getValidation().getIpayErrorDesc());
								result.setStatus(
										(ipayResponse.isSuccess() ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE));
								result.setMessage(msg);
								result.setDetails(msg);
								User u = userApi.findByUserName(user.getUsername());
								result.setBalance(u.getAccountDetail().getBalance());
							} else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Landline bill payment failed. Please try again later.");
								result.setDetails(transactionError.getMessage());
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

	@RequestMapping(value = "/ProcessElectricity", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processElectricity(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody ElectricityBillPaymentDTO electricity,
			@RequestHeader(value = "hash", required = true) String hash, HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(electricity, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				ElectricityBillPaymentError error = new ElectricityBillPaymentError();
				error = electricityBillPaymentValidation.checkError(electricity);
				if (error.isValid()) {
					String sessionId = electricity.getSessionId();
					UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
					if (userSession != null) {
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							PQService service = pqServiceRepository.findServiceByCode(electricity.getServiceProvider());
							TransactionError transactionError = transactionValidation
									.validateBillPayment(electricity.getAmount(), user.getUsername(), service);
							if (transactionError.isValid()) {
								TransactionResponse ipayResponse = topupAndBillPaymentApi
										.electricityBillPayment(electricity, user.getUsername(), service);
								String msg = (ipayResponse.isSuccess() ? ipayResponse.getTransaction().getResMsg()
										: ipayResponse.getValidation().getIpayErrorDesc());
								result.setStatus(
										(ipayResponse.isSuccess() ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE));
								result.setMessage(msg);
								result.setDetails(msg);
								User u = userApi.findByUserName(user.getUsername());
								result.setBalance(u.getAccountDetail().getBalance());
							} else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Electricity bill payment failed. Please try again later.");
								result.setDetails(transactionError.getMessage());
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
					result.setDetails(error);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
				result.setMessage("Failed, Unauthorized user.");
				result.setDetails("Failed, Unauthorized user.");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Failed, Please try again later.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/ProcessGas", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processGas(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody GasBillPaymentDTO gas, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(gas, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				GasBillPaymentError error = new GasBillPaymentError();
				error = gasBillPaymentValidation.checkError(gas);
				if (error.isValid()) {
					String sessionId = gas.getSessionId();
					UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
					if (userSession != null) {
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							PQService service = pqServiceRepository.findServiceByCode(gas.getServiceProvider());
							TransactionError transactionError = transactionValidation
									.validateBillPayment(gas.getAmount(), user.getUsername(), service);
							if (transactionError.isValid()) {
								TransactionResponse ipayResponse = topupAndBillPaymentApi.gasBillPayment(gas,
										user.getUsername(), service);
								String msg = (ipayResponse.isSuccess() ? ipayResponse.getTransaction().getResMsg()
										: ipayResponse.getValidation().getIpayErrorDesc());
								result.setStatus(
										(ipayResponse.isSuccess() ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE));
								result.setMessage(msg);
								result.setDetails(msg);
								User u = userApi.findByUserName(user.getUsername());
								result.setBalance(u.getAccountDetail().getBalance());
							} else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Gas bill payment failed. Please try again later.");
								result.setDetails(transactionError.getMessage());
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

	@RequestMapping(value = "/ProcessInsurance", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processInsurance(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody InsuranceBillPaymentDTO insurance, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(insurance, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				InsuranceBillPaymentError error = new InsuranceBillPaymentError();
				error = insuranceBillPaymentValidation.checkError(insurance);
				if (error.isValid()) {
					String sessionId = insurance.getSessionId();
					UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
					if (userSession != null) {
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							PQService service = pqServiceRepository.findServiceByCode(insurance.getServiceProvider());
							TransactionError transactionError = transactionValidation
									.validateBillPayment(insurance.getAmount(), user.getUsername(), service);
							if (transactionError.isValid()) {
								TransactionResponse ipayResponse = topupAndBillPaymentApi
										.insuranceBillPayment(insurance, user.getUsername(), service);
								String msg = (ipayResponse.isSuccess() ? ipayResponse.getTransaction().getResMsg()
										: ipayResponse.getValidation().getIpayErrorDesc());
								result.setStatus(
										(ipayResponse.isSuccess() ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE));
								result.setMessage(msg);
								result.setDetails(msg);
								User u = userApi.findByUserName(user.getUsername());
								result.setBalance(u.getAccountDetail().getBalance());
							} else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Insurance bill payment failed. Please try again later.");
								result.setDetails(transactionError.getMessage());
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

}
