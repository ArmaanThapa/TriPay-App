package com.tripayapp.controller.api.v1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.instantpay.api.IServicesApi;
import com.tripayapp.entity.*;
import com.tripayapp.model.*;
import com.tripayapp.model.error.*;
import com.tripayapp.repositories.PQServiceRepository;
import com.tripayapp.util.ConvertUtil;
import com.tripayapp.validation.TransactionValidation;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.tripayapp.api.ITransactionApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.mail.util.MailTemplate;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.CommonValidation;
import com.tripayapp.validation.RegisterValidation;

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}")
public class UserController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final IUserApi userApi;
	private final ITransactionApi transactionApi;
	private final RegisterValidation registerValidation;
	private final UserSessionRepository userSessionRepository;
	private final PersistingSessionRegistry persistingSessionRegistry;
	private final TransactionValidation transactionValidation;
	private final PQServiceRepository serviceRepository;
	public UserController(IUserApi userApi, ITransactionApi transactionApi, RegisterValidation registerValidation,
			UserSessionRepository userSessionRepository, PersistingSessionRegistry persistingSessionRegistry,TransactionValidation transactionValidation,PQServiceRepository serviceRepository) {
		this.userApi = userApi;
		this.transactionApi = transactionApi;
		this.registerValidation = registerValidation;
		this.userSessionRepository = userSessionRepository;
		this.persistingSessionRegistry = persistingSessionRegistry;
		this.transactionValidation = transactionValidation;
		this.serviceRepository = serviceRepository;

	}

	@RequestMapping(value = "/AccountUpdateRequest", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ResponseDTO> requestUpdateForKYC(@PathVariable(value = "role") String role,
													@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
													@RequestBody KycDTO dto, @RequestHeader(value = "hash", required = true) String hash,
													HttpServletRequest request, HttpServletResponse response) throws Exception {

		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("User")) {
			String sessionId = dto.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (userSession != null) {
				UserDTO user = userApi.getUserById(userSession.getUser().getId());
				if (user.getAuthority().contains(Authorities.USER)
						&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
					persistingSessionRegistry.refreshLastRequest(sessionId);
					//TODO check if it is already kyc user
					User u = userApi.findByUserName(user.getUsername());
					PQAccountDetail account = u.getAccountDetail();
					if(account != null){
						String code = account.getAccountType().getCode();
						if(code.equalsIgnoreCase("NONKYC")) {
						//TODO check if it already linked with bank account of vijaya bank
							VBankAccountDetail vbankAccount = account.getvBankAccount();
							if(vbankAccount == null) {
								//TODO check the entered account is not being used by anyone
								String accountNumber = SecurityUtil.sha1(dto.getAccountNumber());
								boolean exists = userApi.containsAccountAndMobile(accountNumber,dto.getMobileNumber());
								if(!exists) {
									//TODO validate account and mobile number with KYC API
									KYCResponse kycResponse = userApi.verifyByKycApi(dto);
									boolean isValidKYC = kycResponse.isValid();
									if(isValidKYC) {
										//TODO store values in table

										vbankAccount = new VBankAccountDetail();
										userApi.updateVBankAccountDetails(kycResponse, vbankAccount, u);
										result.setStatus(ResponseStatus.SUCCESS);
										result.setMessage("OTP sent to " + dto.getMobileNumber());
									}else {
										result.setStatus(ResponseStatus.BAD_REQUEST);
										result.setMessage("Account not available in Vijaya Bank");
									}
								}else {
									result.setStatus(ResponseStatus.FAILURE);
									result.setMessage("Your account number is already in use");
								}
							}else {
								Status status = vbankAccount.getStatus();
								if(status == Status.Inactive){
									KYCResponse kycResponse = userApi.verifyByKycApi(dto);
									boolean isValidKYC= kycResponse.isValid();
									if(isValidKYC) {
										userApi.updateVBankAccountDetails(kycResponse, vbankAccount, u);
										result.setStatus(ResponseStatus.SUCCESS);
										result.setMessage("OTP sent to " + dto.getMobileNumber());
									}else {
										result.setStatus(ResponseStatus.BAD_REQUEST);
										result.setMessage("Account not available in Vijaya Bank");
									}
								}else {
									result.setStatus(ResponseStatus.FAILURE);
									result.setMessage("Account Already linked");
								}
							}
						}else {
							result.setStatus(ResponseStatus.FAILURE);
							result.setMessage("You're already a KYC User");
						}
					}else {
						result.setStatus(ResponseStatus.FAILURE);
						result.setMessage("Account Not Exists");
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
			result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
			result.setMessage("Failed, Unauthorized user.");
			result.setDetails("Failed, Unauthorized user.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);

	}

	@RequestMapping(value = "/AccountUpdate", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ResponseDTO> processUpdateForKYC(@PathVariable(value = "role") String role,
													@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
													@RequestBody KycDTO dto, @RequestHeader(value = "hash", required = true) String hash,
													HttpServletRequest request, HttpServletResponse response) throws Exception {

		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("User")) {
			String sessionId = dto.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (userSession != null) {
				UserDTO user = userApi.getUserById(userSession.getUser().getId());
				if (user.getAuthority().contains(Authorities.USER)
						&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
					persistingSessionRegistry.refreshLastRequest(sessionId);
					//TODO check whether OTP is same as that of mobileToken of current user
					User u = userApi.findByUserName(user.getUsername());
					boolean isVerified = userApi.isVerified(dto,u);
					if(isVerified){
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Your Account is Verified Successfully");
					}else {
						result.setStatus(ResponseStatus.FAILURE);
						result.setMessage("OTP doesn't match");
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
			result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
			result.setMessage("Failed, Unauthorized user.");
			result.setDetails("Failed, Unauthorized user.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);

	}


	@RequestMapping(value = "/Validate/Transaction", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ResponseDTO> validateTransaction(@PathVariable(value = "role") String role,
													@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
													@RequestBody TransactionDTO dto, @RequestHeader(value = "hash", required = true) String hash,
													HttpServletRequest request, HttpServletResponse response){

		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("User")) {
			String sessionId = dto.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (userSession != null) {
				UserDTO user = userApi.getUserById(userSession.getUser().getId());
				if (user.getAuthority().contains(Authorities.USER)
						&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
					persistingSessionRegistry.refreshLastRequest(sessionId);
                    dto.setSenderUsername(user.getUsername());
					String transactionRefNo = dto.getTransactionRefNo();
                    boolean isCredit = transactionRefNo.equalsIgnoreCase("C");
					boolean isMerchantPay = transactionRefNo.equalsIgnoreCase("M");
					TransactionError error = null;
					if(isCredit){
						error = transactionValidation.validateGenericTransactionForAPI(dto);
					}else if(isMerchantPay){

					} else{
						error = transactionValidation.validateGenericTransaction(dto);
					}
                    result.setStatus(ResponseStatus.SUCCESS);
                    result.setMessage(error.getMessage());
                    result.setDetails(error);
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
		} else if (role.equalsIgnoreCase("Merchant")) {
			String sessionId = dto.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (userSession != null) {
				UserDTO user = userApi.getUserById(userSession.getUser().getId());
				if (user.getAuthority().contains(Authorities.LOCKED)
						&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
					persistingSessionRegistry.refreshLastRequest(sessionId);
                    dto.setSenderUsername(user.getUsername());
					String transactionRefNo = dto.getTransactionRefNo();
                    boolean isCredit = transactionRefNo.equalsIgnoreCase("C");
					boolean isMerchantPay = transactionRefNo.equalsIgnoreCase("M");
					TransactionError error = null;
					if(isCredit){
						error = transactionValidation.validateGenericTransactionForAPI(dto);
					}else if(isMerchantPay){

					} else{
						error = transactionValidation.validateGenericTransaction(dto);
					}
                    result.setStatus(ResponseStatus.SUCCESS);
                    result.setMessage(error.getMessage());
                    result.setDetails(error);
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
		}
		else {
			result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
			result.setMessage("Failed, Unauthorized user.");
			result.setDetails("Failed, Unauthorized user.");
			}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);

	}

	@RequestMapping(value = "/SharePoints", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ResponseDTO> sharePoints(@PathVariable(value = "role") String role,
													@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
													@RequestBody SharePointDTO dto, @RequestHeader(value = "hash", required = true) String hash,
													HttpServletRequest request, HttpServletResponse response){

		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("User")) {
			String sessionId = dto.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (userSession != null) {
				UserDTO user = userApi.getUserById(userSession.getUser().getId());
				if (user.getAuthority().contains(Authorities.USER)
						&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
					persistingSessionRegistry.refreshLastRequest(sessionId);
					dto.setSenderUsername(user.getUsername());
					PQService service = serviceRepository.findServiceByCode("SPU");
					TransactionError error = transactionValidation.validateSharePoints(dto);
					if(error.isValid()){
						result.setStatus(ResponseStatus.SUCCESS);
						String transactionRefNo = "SP"+System.currentTimeMillis();
						String description = dto.getPoints()+" shared to "+dto.getReceiverUsername()+" from "+dto.getSenderUsername();
						transactionApi.initiateSharePoints(dto.getPoints(),description,transactionRefNo,dto.getSenderUsername(),dto.getReceiverUsername(),dto.toJSON().toString());
						transactionApi.successSharePoints(transactionRefNo);
						result.setMessage("Points Shared Successfully");
					}else {
						result.setStatus(ResponseStatus.BAD_REQUEST);
						result.setMessage("Transaction Error");
						result.setDetails(error);
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
			result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
			result.setMessage("Failed, Unauthorized user.");
			result.setDetails("Failed, Unauthorized user.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);

	}

	@RequestMapping(value = "/EditProfile/Process", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processEditedDetails(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody RegisterDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("User")) {
			String sessionId = dto.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (userSession != null) {
				UserDTO user = userApi.getUserById(userSession.getUser().getId());
				if (user.getAuthority().contains(Authorities.USER)
						&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
					persistingSessionRegistry.refreshLastRequest(sessionId);
					RegisterError error = new RegisterError();
					error = registerValidation.validateEditUser(dto);
					if (error.isValid()) {
						dto.setUsername(user.getUsername());
						userApi.editUser(dto);
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Profile has been edited successfully.");
						result.setDetails("Profile has been edited successfully.");
					} else {
						result.setStatus(ResponseStatus.BAD_REQUEST);
						result.setMessage("Failed, invalid request.");
						result.setDetails(error);
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
			result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
			result.setMessage("Failed, Unauthorized user.");
			result.setDetails("Failed, Unauthorized user.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/UpdatePassword/Process", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ResponseDTO> processNewPassword(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestHeader(value = "hash", required = true) String hash, HttpServletRequest request,
			HttpServletResponse response, @RequestBody ChangePasswordDTO change) {
		ResponseDTO result = new ResponseDTO();
		ChangePasswordError error = registerValidation.validateChangePasswordDTO(change);
		String sessionId = change.getSessionId();
		if (role.equalsIgnoreCase("User")) {
			if (error.isValid()) {
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						userApi.renewPasswordFromAccount(change);
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Password Upadated Successfully.");
						result.setDetails("Password Upadated Successfully.");

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

		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/UploadPicture/Process", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processProfilePicture(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody UploadPictureDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception {
		String stringFile = dto.getP();
		String fileFormat = dto.getF();
		String sessionId = dto.getS();
		System.err.println("Session ::" + sessionId);
		System.err.println("P ::" + stringFile);
		System.err.println("f " + fileFormat);

		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("User")) {
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (userSession != null) {
				UserDTO user = userApi.getUserById(userSession.getUser().getId());
				if (user.getAuthority().contains(Authorities.USER)
						&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
					persistingSessionRegistry.refreshLastRequest(sessionId);
					try {

						userApi.saveImage(userSession.getUser(), dto.getP());
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Profile picture updated successfully.");
						result.setDetails(dto.getP());
					} catch (IllegalStateException e) {
						result.setStatus(ResponseStatus.INTERNAL_SERVER_ERROR);
						result.setMessage("Failed, Please try again later.");
						result.setDetails("Failed, Please try again later.");
					}

				} else {
					result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
					result.setMessage("Failed, Unauthorized user.");
					result.setDetails("Failed, Unauthorized user.");
				}
			} else {
				result.setStatus(ResponseStatus.INVALID_SESSION);
				result.setMessage("Failed, Please try again later.");
				result.setDetails("Failed, Please try again later.");
			}
		} else {
			result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
			result.setMessage("Failed, Unauthorized user.");
			result.setDetails("Failed, Unauthorized user.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}


	@RequestMapping(value = "/VerifyEmail/Process", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> verifyEmail(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody VerifyEmailDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("User")) {
			VerifyEmailError error = new VerifyEmailError();
			error = registerValidation.checkMailError(dto);
			if (error.isValid()) {
				UserSession userSession = userSessionRepository.findByActiveSessionId(dto.getSessionId());
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(dto.getSessionId());
						if (userApi.activateEmail(dto.getKey())) {
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Email is verified successfully.");
							result.setDetails("Email is verified successfully.");
						} else {
							result.setStatus(ResponseStatus.FAILURE);
							result.setMessage("Failed to verify email.");
							result.setDetails("Failed to verify email.");
						}
					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("Failed, Unauthorized user.");
						result.setDetails("Failed, Unauthorized user.");
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Failed, Please try again later.");
					result.setDetails("Failed, Please try again later.");
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
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/GetUserDetails", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getUserDetails(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SessionDTO session, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(session, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				String sessionId = session.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						Map<String, Object> detail = new HashMap<String, Object>();
						User activeUser = userApi.findByUserName(user.getUsername());
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("User details received.");
						detail.put("userDetail", user);
						detail.put("accountDetail", activeUser.getAccountDetail());
						result.setDetails(detail);
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
			}else if(role.equalsIgnoreCase("Admin")){
				String sessionId = session.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						Map<String, Object> detail = new HashMap<String, Object>();
						User activeUser = userApi.findByUserName(user.getUsername());
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Admin details received.");
						detail.put("userDetail", user);
						detail.put("accountDetail", activeUser.getAccountDetail());
						result.setDetails(detail);
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
			} else if(role.equalsIgnoreCase("Merchant")){
				String sessionId = session.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.MERCHANT)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						Map<String, Object> detail = new HashMap<String, Object>();
						User activeUser = userApi.findByUserName(user.getUsername());
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Merchant details received.");
						detail.put("userDetail", user);
						detail.put("accountDetail", activeUser.getAccountDetail());
						result.setDetails(detail);
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

			}else {
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

	@RequestMapping(value = "/Invite/Email", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> inviteByEmail(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody InviteFriendsDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				MobileTopupError error = new MobileTopupError();
				String sessionId = dto.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						userApi.inviteByEmailAddress("Welcome To VPayQwik", MailTemplate.INVITE_FRIEND, dto.getEmail());
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Invitation sent to user");

					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("User | Invite Friend");
						result.setDetails("Permission Not Granted");
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("User | Invite Friend");
					result.setDetails("Invalid Session");
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("User | Invite Friend");
				result.setDetails("Permission Not Granted");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/Invite/Mobile", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> inviteByMobile(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody InviteFriendsDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
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
						User friend = userApi.findByUserName(dto.getMobileNo());
						if(friend == null) {
							User sender = userApi.findByUserName(user.getUsername());
							boolean isSent = userApi.inviteByMobile(dto.getMobileNo(), dto.getMessage(), sender);
							if(isSent) {
								result.setStatus(ResponseStatus.SUCCESS);
								result.setMessage("Invitation Sent Successfully");
								result.setDetails("Invitation Sent Successfully");
							}else {
								result.setStatus(ResponseStatus.BAD_REQUEST);
								result.setMessage("User Already Invited");
								result.setDetails("User Already Invited");
							}
						}else {
							result.setStatus(ResponseStatus.BAD_REQUEST);
							result.setMessage("User Already Exists");
							result.setDetails("User Already Exists");
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
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Permission Not Granted");
				result.setDetails("Permission Not Granted");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}


	@RequestMapping(value = "/GetReceipts", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getUserReceipts(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody PagingDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		System.err.println("IN SIDE");
		System.err.println("11");
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				System.err.println("11");
				UserSession userSession = userSessionRepository.findByActiveSessionId(dto.getSessionId());
				if (userSession != null) {
					System.err.println("11");
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						System.err.println("11");
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							System.err.println("11");
							Sort sort = new Sort(Sort.Direction.DESC, "id");
							Pageable pageable = new PageRequest(dto.getPage(), dto.getSize(), sort);
							Page<PQTransaction> pg = transactionApi.getTotalTransactionsOfUser(pageable,
									user.getUsername());
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("User receipts");
							result.setDetails(pg);
						} else {
							System.err.println("11");
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Failed to get user receipts");
							result.setDetails("Failed to get user receipts");
						}
					}
				} else {
					System.err.println("11");
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Please login and try again.");
					result.setDetails("Please login and try again.");
				}
			} else {
				System.err.println("11");
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Unauthorized user.");
				result.setDetails("Unauthorized user.");
			}
		} else {
			System.err.println("11");
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid request.");
			result.setDetails("Invalid request.");
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@RequestMapping(value = "/STransactions", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getSuccessfulTransactions(@PathVariable(value = "role") String role,
												@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
												@RequestBody SessionDTO dto, @RequestHeader(value = "hash", required = true) String hash,
												HttpServletRequest request, HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		System.err.println("IN SIDE");
		System.err.println("11");
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				System.err.println("11");
				UserSession userSession = userSessionRepository.findByActiveSessionId(dto.getSessionId());
				if (userSession != null) {
					System.err.println("11");
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						System.err.println("11");
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							System.err.println("11");
							List<PQTransaction> transactionList = new ArrayList<>();
							transactionList = transactionApi.getTotalSuccessfulTransactionsOfUser(user.getUsername());
							List<FavouriteDTO> favouriteDTOs = ConvertUtil.convertTransactionList(transactionList);
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("User Transactions");
							result.setDetails(favouriteDTOs);
						} else {
							System.err.println("11");
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Failed to get user receipts");
							result.setDetails("Failed to get user receipts");
						}
					}
				} else {
					System.err.println("11");
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Please login and try again.");
					result.setDetails("Please login and try again.");
				}
			} else {
				System.err.println("11");
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Unauthorized user.");
				result.setDetails("Unauthorized user.");
			}
		} else {
			System.err.println("11");
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid request.");
			result.setDetails("Invalid request.");
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@RequestMapping(value = "/UpdateFavourite", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> setTransactionsAsFavourite(@PathVariable(value = "role") String role,
														  @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
														  @RequestBody FavouriteRequest dto, @RequestHeader(value = "hash", required = true) String hash,
														  HttpServletRequest request, HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		System.err.println("IN SIDE");
		System.err.println("11");
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				System.err.println("11");
				UserSession userSession = userSessionRepository.findByActiveSessionId(dto.getSessionId());
				if (userSession != null) {
					System.err.println("11");
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						System.err.println("11");
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							System.err.println("11");
							long rowsUpdated = transactionApi.updateFavouriteTransaction(dto.getTransactionRefNo(),dto.isFavourite());
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Successfully updated");
							result.setDetails("Rows updated "+rowsUpdated);
						} else {
							System.err.println("11");
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Failed to get user receipts");
							result.setDetails("Failed to get user receipts");
						}
					}
				} else {
					System.err.println("11");
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Please login and try again.");
					result.setDetails("Please login and try again.");
				}
			} else {
				System.err.println("11");
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Unauthorized user.");
				result.setDetails("Unauthorized user.");
			}
		} else {
			System.err.println("11");
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid request.");
			result.setDetails("Invalid request.");
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}



	@RequestMapping(value = "/ReSendEmailOTP", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processResendEmailOTP(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SessionDTO session, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		System.err.println("session ID :: " + session.getSessionId());

		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(session, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				MobileTopupError error = new MobileTopupError();
				String sessionId = session.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)) {
						User userEmail = userApi.findByUserName(user.getUsername());
						userApi.reSendEmailOTP(userEmail);
						Map<String, Object> detail = new HashMap<String, Object>();
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("User details received.");
						detail.put("userDetail", user);
						result.setDetails(detail);
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

	@RequestMapping(value = "/ChangeEmail", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processChangeEmail(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody ChangeEmailDTO session, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		System.err.println("session ID :: " + session.getSessionId());

		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(session, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				MobileTopupError error = new MobileTopupError();
				String sessionId = session.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)) {
						User userEmail = userApi.findByUserName(user.getUsername());
						boolean isValidEmail = CommonValidation.isValidMail(session.getEmail());
						if (isValidEmail) {
							userApi.changeEmail(userEmail, session.getEmail());
							Map<String, Object> detail = new HashMap<String, Object>();
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Email ID updated Successfully");
							UserDTO updatedUser = userApi.getUserById(userSession.getUser().getId());
							detail.put("userDetail", updatedUser);
							result.setDetails(detail);
						}
						result.setStatus(ResponseStatus.BAD_REQUEST);
						result.setMessage("Email, Invaild Email ID");

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

	@RequestMapping(value="/GetTransactionDTO",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<OnePayResponse> processOnePayRequest(@PathVariable(value = "role") String role,
														@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
														@RequestBody OnePayRequest dto, @RequestHeader(value = "hash", required = true) String hash,
														HttpServletRequest request, ModelMap modelMap, HttpServletResponse response){
		OnePayResponse result = new OnePayResponse();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				String sessionId = dto.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)) {
								result = transactionApi.getTransaction(dto.getTransactionRefNo());
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
				result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
				result.setMessage("Failed, Unauthorized user.");
				result.setDetails("Failed, Unauthorized user.");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Failed, Please try again later.");
		}
	return new ResponseEntity<OnePayResponse>(result,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/Validate/MTransaction", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ResponseDTO> validateMerchantTransaction(@PathVariable(value = "role") String role,
													@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
													@RequestBody TransactionDTO dto, @RequestHeader(value = "hash", required = true) String hash,
													HttpServletRequest request, HttpServletResponse response){

		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("Merchant")) {
			String sessionId = dto.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (userSession != null) {
				UserDTO user = userApi.getUserById(userSession.getUser().getId());
				if (user.getAuthority().contains(Authorities.MERCHANT)
						&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
					persistingSessionRegistry.refreshLastRequest(sessionId);
                    dto.setSenderUsername(user.getUsername());
					String transactionRefNo = dto.getTransactionRefNo();
                    boolean isCredit = transactionRefNo.equalsIgnoreCase("C");
					boolean isMerchantPay = transactionRefNo.equalsIgnoreCase("M");
					TransactionError error = null;
					if(isCredit){
						error = transactionValidation.validateGenericTransactionForAPI(dto);
					}else if(isMerchantPay){

					} else{
						error = transactionValidation.validateGenericTransaction(dto);
					}
                    result.setStatus(ResponseStatus.SUCCESS);
                    result.setMessage(error.getMessage());
                    result.setDetails(error);
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
		} else if (role.equalsIgnoreCase("Merchant")) {
			String sessionId = dto.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (userSession != null) {
				UserDTO user = userApi.getUserById(userSession.getUser().getId());
				if (user.getAuthority().contains(Authorities.LOCKED)
						&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
					persistingSessionRegistry.refreshLastRequest(sessionId);
                    dto.setSenderUsername(user.getUsername());
					String transactionRefNo = dto.getTransactionRefNo();
                    boolean isCredit = transactionRefNo.equalsIgnoreCase("C");
					boolean isMerchantPay = transactionRefNo.equalsIgnoreCase("M");
					TransactionError error = null;
					if(isCredit){
						error = transactionValidation.validateGenericTransactionForAPI(dto);
					}else if(isMerchantPay){

					} else{
						error = transactionValidation.validateGenericTransaction(dto);
					}
                    result.setStatus(ResponseStatus.SUCCESS);
                    result.setMessage(error.getMessage());
                    result.setDetails(error);
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
		}
		else {
			result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
			result.setMessage("Failed, Unauthorized user.");
			result.setDetails("Failed, Unauthorized user.");
			}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);

	}

	
	
}
