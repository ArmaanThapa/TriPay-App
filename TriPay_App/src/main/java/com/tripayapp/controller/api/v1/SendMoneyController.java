package com.tripayapp.controller.api.v1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tripayapp.entity.*;
import com.tripayapp.model.*;
import com.tripayapp.repositories.BankDetailRepository;
import com.tripayapp.repositories.BanksRepository;
import com.tripayapp.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tripayapp.api.ISendMoneyApi;
import com.tripayapp.api.IUserApi;
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

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}/SendMoney")
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
	private final BanksRepository banksRepository;
	private final BankDetailRepository bankDetailRepository;

	public SendMoneyController(ISendMoneyApi sendMoneyApi, IUserApi userApi, SendMoneyValidation sendMoneyValidation,
			TransactionValidation transactionValidation, UserSessionRepository userSessionRepository,
			PersistingSessionRegistry persistingSessionRegistry, PQServiceRepository pqServiceRepository,BanksRepository banksRepository,BankDetailRepository bankDetailRepository) {
		this.sendMoneyApi = sendMoneyApi;
		this.userApi = userApi;
		this.sendMoneyValidation = sendMoneyValidation;
		this.transactionValidation = transactionValidation;
		this.userSessionRepository = userSessionRepository;
		this.persistingSessionRegistry = persistingSessionRegistry;
		this.pqServiceRepository = pqServiceRepository;
		this.banksRepository = banksRepository;
		this.bankDetailRepository = bankDetailRepository;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}


	@RequestMapping(value = "/Merchant/Mobile", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processMerchantMobile(@PathVariable(value = "role") String role,
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
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							PGDetails pgDetails = userApi.findMerchantByMobile(mobile.getMobileNumber());
							if(pgDetails != null) {
								PQService service = pgDetails.getService();
								if (service != null) {
									error = sendMoneyValidation.checkMobileError(mobile, user.getUsername(), service.getCode());
									if (error.isValid()) {
										TransactionError transactionError = transactionValidation.validateP2PTransaction(
												mobile.getAmount(), user.getUsername(), mobile.getMobileNumber(), service);
										if (transactionError.isValid()) {
											User u = userApi.findByUserName(user.getUsername());
											User merchant = pgDetails.getUser();
											PayStoreDTO pay = ConvertUtil.convertToPayStore(merchant.getId(),mobile.getAmount());
											result = sendMoneyApi.preparePayStore(pay,u);
											result.setBalance(userApi.getWalletBalance(u));

										} else {
											result.setStatus(ResponseStatus.FAILURE);
											result.setMessage(transactionError.getMessage());
											result.setDetails(transactionError.getMessage());
										}
									} else {
										result.setStatus(ResponseStatus.BAD_REQUEST);
										result.setMessage("Failed, invalid request.");
										result.setDetails(error);
									}
								}else {
									result.setStatus(ResponseStatus.FAILURE);
									result.setMessage("Service Not Found For Merchant");
								}
							}else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Merchant Not Found");
								result.setDetails("Merchant Not Found");
							}
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Failed, Unauthorized user.");
							result.setDetails("Failed, Unauthorized user.");
						}
					}else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Invalid Session,Please try again later");
					result.setDetails("Invalid Session,Please try again later");
				}
				} else {
					result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
					result.setMessage("Failed, Unauthorized User");
					result.setDetails("Failed, Unauthorized User");
				}
			} else {
				result.setStatus(ResponseStatus.INVALID_HASH);
				result.setMessage("Not a valid hash");
				result.setDetails("Not a valid hash");
			}

		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
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
								result.setMessage(transactionError.getMessage());
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
		System.err.println("bank request initiated");
		boolean isValidHash = SecurityUtil.isHashMatches(device, hash);
		if (isValidHash) {
			System.err.println("valid hash");
				if (role.equalsIgnoreCase("User")) {
					System.err.println("role is user");
				SendMoneyBankError error = new SendMoneyBankError();
				error = sendMoneyValidation.checkBankError(bank, "SMB");
				if (error.isValid()) {
					System.err.println("error is valid");
					String sessionId = bank.getSessionId();
					UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
					if (userSession != null) {
						System.err.println("user session is not null");
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							System.err.println("authority is role user");
							persistingSessionRegistry.refreshLastRequest(sessionId);
							PQService service = pqServiceRepository.findServiceByCode("SMB");
							User u = userApi.findByUserName(user.getUsername());
							String accountType = u.getAccountDetail().getAccountType().getCode();
							if(accountType.equalsIgnoreCase("KYC")) {
								TransactionError transactionError = transactionValidation.validateMerchantTransaction(bank.getAmount(), user.getUsername(), service);
								if (transactionError.isValid()) {
									Banks banks = banksRepository.findByCode(bank.getBankCode());
									if(banks != null) {
										BankDetails bankDetail = bankDetailRepository.findByIfscCode(bank.getIfscCode(),banks);
										if(bankDetail != null) {
											sendMoneyApi.sendMoneyBankInitiate(bank, user.getUsername(), service);
											result.setStatus(ResponseStatus.SUCCESS);
											result.setMessage("Your request is initiated successfully for " + bank.getAccountNumber() + "  where account holder is" + bank.getAccountName() + ".\n Amount will be credited within 24 hours");
											result.setDetails("");
											if (u != null) {
												result.setBalance(u.getAccountDetail().getBalance());
											}
										}else {
											result.setStatus(ResponseStatus.FAILURE);
											result.setMessage("IFSC Code is not valid");
										}
									}else {
										result.setStatus(ResponseStatus.FAILURE);
										result.setMessage("Bank Code is not valid");
									}
								} else {
									result.setStatus(ResponseStatus.FAILURE);
									result.setMessage(transactionError.getMessage());
									result.setDetails(transactionError.getMessage());
								}
							}else{
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("You must be KYC user to use this functionality,\n Please contact Customer Care");
								result.setDetails("You must be KYC user to use this functionality,\n Please contact Customer Care");
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

	
	@RequestMapping(value = "/MBankTransfer/Initiate", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> initiateMBankTransfer(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SendMoneyBankDTO bank, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		SendMoneyResponse resp = new SendMoneyResponse();
		System.err.println(" Merchant bank transfer request initiated");
		boolean isValidHash = SecurityUtil.isHashMatches(device, hash);
		if (isValidHash) {
			System.err.println("valid hash");
				if (role.equalsIgnoreCase("Merchant")) {
					System.err.println("role is Merchant");
				SendMoneyBankError error = new SendMoneyBankError();
				error = sendMoneyValidation.checkBankError(bank, "SMMB");
				if (error.isValid()) {
					System.err.println("error is valid");
					String sessionId = bank.getSessionId();
					UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
					if (userSession != null) {
						System.err.println("user session is not null");
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						if (user.getAuthority().contains(Authorities.MERCHANT)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							System.err.println("authority is role Merchant");
							persistingSessionRegistry.refreshLastRequest(sessionId);
							PQService service = pqServiceRepository.findServiceByCode("SMMB");
							User u = userApi.findByUserName(user.getUsername());
								resp = sendMoneyApi.checkInputParameters(bank);
								System.out.println(resp.getCode());
								if(resp.isValid()) {
									TransactionError transactionError = transactionValidation.validateMerchantTransaction(bank.getAmount(), user.getUsername(), service);
									if (transactionError.isValid()) {
										resp = sendMoneyApi.sendMoneyMBankInitiate(bank, user.getUsername(), service);
										System.err.println("Response : " +resp.getCode());
										if(resp.isValid()) {
											result.setStatus(ResponseStatus.SUCCESS);
											result.setMessage("Your request is initiated successfully for " + bank.getAccountNumber() + "  where account holder is" + bank.getAccountName()+ ".\n Amount will be credited within 24 hours");
											result.setBalance(u.getAccountDetail().getBalance());   
										} else {
											result.setStatus(ResponseStatus.FAILURE);
											result.setMessage("Bank Transfer Not Avaliable, Please try again later");
									    }
										return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
									} else {
										result.setStatus(ResponseStatus.FAILURE);
										result.setMessage(transactionError.getMessage());
									}
								  } else {
									  	result.setStatus(ResponseStatus.FAILURE);
									  	result.setMessage(resp.getMessage());
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
}
