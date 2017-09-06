package com.tripayapp.controller.api.v1;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.instantpay.api.IServicesApi;
import com.tripayapp.entity.*;
import com.tripayapp.model.*;
import com.tripayapp.model.Status;
import com.tripayapp.repositories.*;
import com.tripayapp.util.*;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tripayapp.api.IMerchantApi;
import com.tripayapp.api.ISessionApi;
import com.tripayapp.api.ITransactionApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.model.error.MobileTopupError;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.validation.CommonValidation;

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}")
public class AjaxController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final IUserApi userApi;
	private final IMerchantApi merchantApi;
	private final ITransactionApi transactionApi;
	private final UserSessionRepository userSessionRepository;
	private final ISessionApi sessionApi;
	private final PQServiceRepository pqServiceRepository;
	private final BanksRepository banksRepository;
	private final BankDetailRepository bankDetailRepository;
	private final PQServiceTypeRepository pqServiceTypeRepository;
	private final PQCommissionRepository pqCommissionRepository;
	private final SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
	private final SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public AjaxController(IUserApi userApi, ITransactionApi transactionApi, UserSessionRepository userSessionRepository,
			ISessionApi sessionApi, IMerchantApi merchantApi,PQServiceRepository pqServiceRepository,BanksRepository banksRepository,BankDetailRepository bankDetailRepository,PQServiceTypeRepository pqServiceTypeRepository,PQCommissionRepository pqCommissionRepository) {
		this.userApi = userApi;
		this.transactionApi = transactionApi;
		this.userSessionRepository = userSessionRepository;
		this.sessionApi = sessionApi;
		this.merchantApi = merchantApi;
		this.pqServiceRepository = pqServiceRepository;
		this.banksRepository = banksRepository;
		this.bankDetailRepository = bankDetailRepository;
		this.pqServiceTypeRepository = pqServiceTypeRepository;
		this.pqCommissionRepository = pqCommissionRepository;
	}

	@RequestMapping(value = "/getTotalUsers", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getUserPages(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody PagingDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response)
					throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("Admin")) {
				UserSession userSession = userSessionRepository.findByActiveSessionId(dto.getSessionId());
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							Sort sort = new Sort(Sort.Direction.DESC, "id");
							Pageable pageable = new PageRequest(dto.getPage(), dto.getSize(), sort);
							String strUserStatus = dto.getUserStatus();
							UserStatus userStatus = UserStatus.getEnum(strUserStatus);
							Page<User> pg = null;
							switch (userStatus.getValue()) {
							case "Online":
								pg = sessionApi.findOnlineUsers(pageable);
								break;
							case "Active":
								pg = userApi.getActiveUsers(pageable);
								break;
							case "Blocked":
								pg = userApi.getBlockedUsers(pageable);
								break;
							case "Inactive":
								pg = userApi.getInactiveUsers(pageable);
								break;
							case "Locked":
								pg = userApi.getLockedUsers(pageable);
								break;
								default:
								pg = userApi.getTotalUsers(pageable);
								break;
							}
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Ajax | User List");
							result.setDetails(pg);
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Ajax | User List");
							result.setDetails("Permission Not Granted");
						}
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Ajax | User List");
					result.setDetails("Invalid Session");
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Ajax | User List");
				result.setDetails("Permission Not Granted");

			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@RequestMapping(value = "/getReports", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getReports(@PathVariable(value = "role") String role,
											 @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
											 @RequestBody PagingDTO dto, @RequestHeader(value = "hash", required = true) String hash,
											 HttpServletRequest request, HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException, ParseException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("Admin")) {
				UserSession userSession = userSessionRepository.findByActiveSessionId(dto.getSessionId());
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							String reportType = dto.getReportType();
							List<TransactionReport> reports = null;
							List<PQTransaction> transactions = new ArrayList<>();
							String startDate = date.format(dto.getStartDate())+" 00:00:00";
							String endDate = date.format(dto.getEndDate())+" 23:59:59";
							dto.setStartDate(dateTime.parse(startDate));
							dto.setEndDate(dateTime.parse(endDate));
							String serviceType = "";
							boolean debit = false;
							switch(reportType.toUpperCase()){
								case "TOPUPBILL":
									serviceType = "Bill Payment";
									debit = true;
									break;
								case "LOADMONEY":
									serviceType = "Load Money";
									debit = false;
									break;
								default:
									serviceType = "Load Money";
									debit = false;
									break;
							}
							PQServiceType type = pqServiceTypeRepository.findServiceTypeByName(serviceType);
							transactions = transactionApi.listTransactionByServiceAndDate(dto.getStartDate(),dto.getEndDate(),type,debit,TransactionType.DEFAULT, Status.Success);
							reports = ConvertUtil.getFromTransactionList(transactions,type);
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage(reportType+" List");
							result.setDetails(reports);
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Ajax | User List");
							result.setDetails("Permission Not Granted");
						}
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Ajax | User List");
					result.setDetails("Invalid Session");
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Ajax | User List");
				result.setDetails("Permission Not Granted");

			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@RequestMapping(value = "/getTotalTransactions", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getTransactionPages(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody PagingDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response)
					throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("Admin")) {
				UserSession userSession = userSessionRepository.findByActiveSessionId(dto.getSessionId());
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							Sort sort = new Sort(Sort.Direction.DESC, "id");
							Pageable pageable = new PageRequest(dto.getPage(), dto.getSize(), sort);
							Page<PQTransaction> pg = transactionApi.getTotalTransactions(pageable);
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Ajax | Transaction List");
							result.setDetails(pg);
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Ajax | Transaction List");
							result.setDetails("Permission Not Granted");
						}
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Ajax | Transaction List");
					result.setDetails("Invalid Session");
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Ajax | Transaction List");
				result.setDetails("Permission Not Granted");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@RequestMapping(value = "/getTotalTransactionsByType", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getTransactionPagesByType(@PathVariable(value = "role") String role,
													@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
													@RequestBody PagingDTO dto, @RequestHeader(value = "hash", required = true) String hash,
													HttpServletRequest request, HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("Admin")) {
				UserSession userSession = userSessionRepository.findByActiveSessionId(dto.getSessionId());
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							Sort sort = new Sort(Sort.Direction.DESC, "id");
							List<PQTransaction> transactionList = transactionApi.listTransactionByPaging(dto);
							List<User> userList = userApi.getAllUsers();
							List<User> filteredList = ConvertUtil.filteredList(userList,dto.getReportType());
							List<PQCommission> commissionList = (List<PQCommission>) pqCommissionRepository.findAll();
							List<TransactionListDTO> resultList = ConvertUtil.convertFromLists(filteredList,transactionList,commissionList);
							Collections.sort(resultList,new TransactionComparator());
							Pageable page = new PageRequest(dto.getPage(),dto.getSize(),sort);
							int start = page.getOffset();
							int end = (start + page.getPageSize()) > resultList.size() ? resultList.size() : (start + page.getPageSize());
							List<TransactionListDTO> subList = resultList.subList(start,end);
							Page<TransactionListDTO> resultSet = new PageImpl<TransactionListDTO>(subList,page,resultList.size());
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Ajax | Transaction List");
							result.setDetails(resultSet);
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Ajax | Transaction List");
							result.setDetails("Permission Not Granted");
						}
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Ajax | Transaction List");
					result.setDetails("Invalid Session");
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Ajax | Transaction List");
				result.setDetails("Permission Not Granted");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@RequestMapping(value = "/getUserTransactions", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getTransactionsOfUser(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody PagingDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response)
					throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				UserSession userSession = userSessionRepository.findByActiveSessionId(dto.getSessionId());
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							Sort sort = new Sort(Sort.Direction.DESC, "id");
							Pageable pageable = new PageRequest(dto.getPage(), dto.getSize(), sort);
							Page<PQTransaction> pg = transactionApi.getTotalTransactionsOfUser(pageable,
									user.getUsername());
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("User transaction success.");
							result.setDetails(pg);
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Failed, Unauthorized user.");
							result.setDetails("Failed, Unauthorized user.");
						}
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
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/{username}/getTransactions", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getTransactionsByUser(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@PathVariable("username") String username, @RequestBody PagingDTO dto,
			@RequestHeader(value = "hash", required = true) String hash, HttpServletRequest request,
			HttpServletResponse response)
					throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("Admin")) {
				UserSession userSession = userSessionRepository.findByActiveSessionId(dto.getSessionId());
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							Sort sort = new Sort(Sort.Direction.DESC, "id");
							Pageable pageable = new PageRequest(dto.getPage(), dto.getSize(), sort);
							Page<PQTransaction> pg = transactionApi.getTotalTransactionsOfUser(pageable, username);
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Ajax | Transactions Of User");
							result.setDetails(pg);
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Ajax | Transactions Of User");
							result.setDetails("Permission Not Granted");
						}
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Ajax | Transactions Of User");
					result.setDetails("Invalid Session");
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Ajax | Transactions Of User");
				result.setDetails("Permission Not Granted");

			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/getTransactions", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getTransactionsByUserPost(@PathVariable(value = "role") String role,
													  @PathVariable(value = "device") String device, @PathVariable(value = "language") String language, @RequestBody PagingDTO dto,
													  @RequestHeader(value = "hash", required = true) String hash, HttpServletRequest request,
													  HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("Admin")) {
				UserSession userSession = userSessionRepository.findByActiveSessionId(dto.getSessionId());
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							Sort sort = new Sort(Sort.Direction.DESC, "id");
							Pageable pageable = new PageRequest(dto.getPage(), dto.getSize(), sort);
							Page<PQTransaction> pg = transactionApi.getTotalTransactionsOfUser(pageable, dto.getUserStatus());
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Ajax | Transactions Of Merchant");
							result.setDetails(pg);
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Ajax | Transactions Of Merchant");
							result.setDetails("Permission Not Granted");
						}
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Ajax | Transactions Of User");
					result.setDetails("Invalid Session");
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Ajax | Transactions Of User");
				result.setDetails("Permission Not Granted");

			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@RequestMapping(value = "/getMerchants", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getMerchants(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SessionDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response)
					throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				UserSession userSession = userSessionRepository.findByActiveSessionId(dto.getSessionId());
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							String type = "STORE";
							List<MerchantDTO> merchantList = merchantApi.getAll(type);
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Merchant List");
							result.setDetails(merchantList);
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Permission Not Granted");
							result.setDetails("Permission Not Granted");
						}
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
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@RequestMapping(value = "/getServices", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getService(@PathVariable(value = "role") String role,
											 @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
											  @RequestHeader(value = "hash", required = true) String hash,
											 HttpServletRequest request, HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		List<PQService> serviceList = (List<PQService>)pqServiceRepository.findAll();
		result.setStatus(ResponseStatus.SUCCESS);
		result.setMessage("Service List");
		result.setDetails(serviceList);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/GetServiceTypes", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getServiceTypes(@PathVariable(value = "role") String role,
										   @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
										   @RequestHeader(value = "hash", required = true) String hash,
										   HttpServletRequest request, HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		List<PQServiceType> serviceList = (List<PQServiceType>)pqServiceTypeRepository.findAll();
		result.setStatus(ResponseStatus.SUCCESS);
		result.setMessage("Service List");
		result.setDetails(ConvertUtil.convertServiceType(serviceList));
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/GetService/{id}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getServiceByTypes(@PathVariable(value = "role") String role,
												@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,@PathVariable(value="id") long id,
												@RequestHeader(value = "hash", required = true) String hash,
												HttpServletRequest request, HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		List<PQService> serviceList = pqServiceRepository.findServiceByServiceTypeID(id);
		result.setStatus(ResponseStatus.SUCCESS);
		result.setMessage("Service List");
		result.setDetails(ConvertUtil.convertService(serviceList));
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

    @RequestMapping(value = "/GetAllServices", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<ResponseDTO> getAllService(@PathVariable(value = "role") String role,
                                                  @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
                                                  @RequestHeader(value = "hash", required = true) String hash,
                                                  HttpServletRequest request, HttpServletResponse response)
            throws JSONException, JsonGenerationException, JsonMappingException, IOException {
        ResponseDTO result = new ResponseDTO();
        List<PQService> serviceList = (List<PQService>) pqServiceRepository.findAll();
        result.setStatus(ResponseStatus.SUCCESS);
        result.setMessage("Service List");
        result.setDetails(ConvertUtil.convertService(serviceList));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

	@RequestMapping(value = "/listBanks", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getBanks(@PathVariable(value = "role") String role,
										   @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
										   @RequestHeader(value = "hash", required = true) String hash,
										   HttpServletRequest request, HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		List<Banks> bankList = (List<Banks>)banksRepository.findAll();
		result.setStatus(ResponseStatus.SUCCESS);
		result.setMessage("Banks List");
		result.setDetails(bankList);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@RequestMapping(value = "/listIFSC/{bankCode}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> listIfsc(@PathVariable(value = "role") String role,
										 @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
										 @PathVariable(value = "bankCode") String bankCode,@RequestHeader(value = "hash", required = true) String hash,
										 HttpServletRequest request, HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		Banks bank = banksRepository.findByCode(bankCode);
		List<String> ifscList = bankDetailRepository.getIFSCFromBank(bank);
		result.setStatus(ResponseStatus.SUCCESS);
		result.setMessage("IFSC List");
		result.setDetails(ifscList);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	@RequestMapping(value = "/{username}/getMTransactions", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getSingleMerchantTransactions(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody PagingDTO dto,@PathVariable("username") String username, 
			@RequestHeader(value = "hash", required = true) String hash, HttpServletRequest request,
			HttpServletResponse response)
					throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("Admin")) {
				UserSession userSession = userSessionRepository.findByActiveSessionId(dto.getSessionId());
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							Sort sort = new Sort(Sort.Direction.DESC, "id");
							//TODO find user using username
							
							System.err.println("USERNAME OF MERCHANT ::" + username);
							User merchantUser = userApi.findByUserName( username);
							System.err.println("MERCHANT LIST ::"+ merchantUser);
							// TODO create new arraylist
							List<User> merchantlist = new ArrayList<>();
							merchantlist.add(merchantUser);
							System.err.println("merchant ::" + merchantlist);
							// TODO insert that user into list
							List<PQTransaction> transList = transactionApi.getTotalTransactionsOfMerchant(username);
//							List<User> userList = userApi.getAllUsers();
//							List<User> filteredList = ConvertUtil.filteredList(userList,dto.getReportType());
							List<PQCommission> commissionList = (List<PQCommission>) pqCommissionRepository.findAll();
							List<TransactionListDTO> resultList = ConvertUtil.convertFromLists(merchantlist,transList,commissionList);
							Collections.sort(resultList,new TransactionComparator());
							Pageable page = new PageRequest(dto.getPage(),dto.getSize(),sort);
							int start = page.getOffset();
							int end = (start + page.getPageSize()) > resultList.size() ? resultList.size() : (start + page.getPageSize());
							List<TransactionListDTO> subList = resultList.subList(start,end);
							Page<TransactionListDTO> resultSet = new PageImpl<TransactionListDTO>(subList,page,resultList.size());
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Ajax | Transaction List");
							result.setDetails(resultSet);
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Ajax | Transactions Of Merchant");
							result.setDetails("Permission Not Granted");
						}
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Ajax | Transactions Of Merchant");
					result.setDetails("Invalid Session");
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Ajax | Transactions Of Merchant");
				result.setDetails("Permission Not Granted");

			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	
	//======================= Test ============================
	
		@RequestMapping(value = "/test", method = RequestMethod.POST, produces = {
				MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
		ResponseEntity<ResponseDTO> test(@PathVariable(value = "role") String role,
				@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
				@RequestBody MobileTopupDTO topup, @RequestHeader(value = "hash", required = true) String hash,
				HttpServletRequest request, HttpServletResponse response) {
			ResponseDTO result = new ResponseDTO();
			boolean isValidHash = SecurityUtil.isHashMatches(topup, hash);
			if (isValidHash) {
				if (role.equalsIgnoreCase("User")) {
					User u = userApi.findByUserName(topup.getSessionId());
					
//					System.err.println("Total Transaction Amount :: " + transactionApi.getMonthlyDebitTransactionTotalAmount(u.getAccountDetail()));
//					List<PQTransaction> tras = transactionApi.getDailyDebitTransaction(u.getAccountDetail());
//					List<PQTransaction> tras = transactionApi.getMonthlyDebitTransaction(u.getAccountDetail());
//					List<PQTransaction> tras = transactionApi.getDailyCreditAndDebitTransation(u.getAccountDetail());
//					List<PQTransaction> tras = transactionApi.getDailyCreditTransation(u.getAccountDetail());
//					List<PQTransaction> tras = transactionApi.getMonthlyCreditTransation(u.getAccountDetail());
					
//					System.err.println("Total Transaction Amount :: " + transactionApi.getDailyDebitTransactionTotalAmount(u.getAccountDetail()));
////					
//					double amount = 0;
//					for (PQTransaction pqTransaction : tras) {
//						System.err.println("Date :: " + pqTransaction.getCreated() + " Description : " + pqTransaction.getDescription() +" Amount " + pqTransaction.getAmount());
//						amount =pqTransaction.getAmount()+amount;
//					}
//					System.err.println("Total Tansaction Amount with :: " + amount);
					if(u!=null){
						double amount =  transactionApi.getLastSuccessTransaction(u.getAccountDetail());
						System.err.println("Last Transaction Balance :: " + amount);
					}
					else{
						System.err.println("No User Found");
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
