package com.tripayapp.controller.api.v2;

import com.tripayapp.api.IMerchantApi;
import com.tripayapp.api.ISessionApi;
import com.tripayapp.api.ITransactionApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.PQTransaction;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.*;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/Api/v2/{role}/{device}/{language}")
public class AjaxController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final IUserApi userApi;
	private final IMerchantApi merchantApi;
	private final ITransactionApi transactionApi;
	private final UserSessionRepository userSessionRepository;
	private final ISessionApi sessionApi;

	public AjaxController(IUserApi userApi, ITransactionApi transactionApi, UserSessionRepository userSessionRepository,
			ISessionApi sessionApi, IMerchantApi merchantApi) {
		this.userApi = userApi;
		this.transactionApi = transactionApi;
		this.userSessionRepository = userSessionRepository;
		this.sessionApi = sessionApi;
		this.merchantApi = merchantApi;
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
							result.setMessage("Ajax | Merchant List");
							result.setDetails(merchantList);
						} else {
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Ajax | Merchant List");
							result.setDetails("Permission Not Granted");
						}
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Ajax | Merchant List");
					result.setDetails("Invalid Session");
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Ajax | Merchant List");
				result.setDetails("Permission Not Granted");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
