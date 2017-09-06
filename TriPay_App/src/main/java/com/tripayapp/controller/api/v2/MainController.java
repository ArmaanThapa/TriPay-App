package com.tripayapp.controller.api.v2;

import com.tripayapp.api.ISessionLogApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.api.impl.TransactionApi;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.SessionDTO;
import com.tripayapp.model.TransactionDTO;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Api/v2/{role}/{device}/{language}")
public class MainController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final IUserApi userApi;
	private final UserSessionRepository userSessionRepository;
	private final ISessionLogApi sessionLogApi;
	private final TransactionApi transactionApi;

	public MainController(IUserApi userApi, UserSessionRepository userSessionRepository, ISessionLogApi sessionLogApi,
			TransactionApi transactionApi) {
		this.userApi = userApi;
		this.userSessionRepository = userSessionRepository;
		this.sessionLogApi = sessionLogApi;
		this.transactionApi = transactionApi;
	}

	@RequestMapping(value = "/Authenticate/SessionId", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> authenticateUser(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SessionDTO session, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(session, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				String sessionId = session.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					boolean requestValid = sessionLogApi.checkRequest(sessionId, request);
					if (requestValid) {
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Session valid");
						Map<String, Object> detail = new HashMap<String, Object>();
						detail.put("authority", user.getAuthority());
						detail.put("user", user);
						result.setDetails(detail);
					} else {
						result.setStatus(ResponseStatus.BAD_REQUEST);
						result.setMessage("Failed, Please try again later.");
						Map<String, Object> detail = new HashMap<String, Object>();
						// detail.put("authority", null);
						detail.put("user", null);
						result.setDetails(detail);
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Please, login and try again.");
					Map<String, Object> detail = new HashMap<String, Object>();
					// detail.put("authority", null);
					detail.put("user", null);
					result.setDetails(detail);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
				result.setMessage("Failed, Unauthorized user.");
				Map<String, Object> detail = new HashMap<String, Object>();
				// detail.put("authority", null);
				detail.put("user", null);
				result.setDetails(detail);
			}
		} else {
			result.setStatus(ResponseStatus.INVALID_HASH);
			result.setMessage("Failed, Please try again later.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/TestDTH", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> addCurrency(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody TransactionDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		// boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (true) {
			if (role.equalsIgnoreCase("User")) {
				String sessionId = dto.getSessionId();
				System.err.println("SessionId: " + sessionId);
				User user1 = userApi.findByUserName(dto.getSenderUsername());
//				List<PQTransaction> transactions = user1.getAccountDetail().getTransactions();
//				for (PQTransaction pqTransaction : transactions) {
//					System.err.println("dddddddd");
//					PQService service = pqTransaction.getService();
//					System.err.println("Serive7777 " + service.getName());
//					
//				}
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					Map<String, Object> detail = new HashMap<String, Object>();
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					System.err.println("User Contact NO: " + user.getContactNo());
					if (user != null) {
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
//							transactionApi.initiateBillPaymentNew(dto.getAmount(), dto.getDescription(), service,
//									dto.getTransactionRefNo(), dto.getReceiverUsername(), dto.getSenderUsername());
						}
						
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Bill Payment Initiated");
						detail.put("sessionId", userSession.getSessionId());
						result.setDetails(detail);
						System.err.println("Bill Payment Initiated");
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("Failed, Unauthorized user.");
						result.setDetails(null);
						return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
					}
				}
			}
		} else {
			result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
			result.setMessage("Failed, Unauthorized user.");
			result.setDetails(null);
			System.err.println("Currency Not Added");
			return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
		}

		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}
}
