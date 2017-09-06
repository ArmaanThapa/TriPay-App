package com.tripayapp.controller.api.v2;

import com.tripayapp.api.ISendMoneyApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.PayAtStoreDTO;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.error.PayAtStoreError;
import com.tripayapp.model.error.TransactionError;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.PQServiceRepository;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.PayStoreValidation;
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
@RequestMapping("/Api/v2/{role}/{device}/{language}/PayAtStore")
public class PayAtStoreController implements MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MessageSource messageSource;
	
	private final PayStoreValidation payStoreValidation;
	private final ISendMoneyApi sendMoneyApi;
	private final IUserApi userApi;
	private final TransactionValidation transactionValidation;
	private final UserSessionRepository userSessionRepository;
	private final PersistingSessionRegistry persistingSessionRegistry;
	private final PQServiceRepository pqServiceRepository;

	public PayAtStoreController(PayStoreValidation payStoreValidation, ISendMoneyApi sendMoneyApi, IUserApi userApi,
			TransactionValidation transactionValidation, UserSessionRepository userSessionRepository,
			PersistingSessionRegistry persistingSessionRegistry, PQServiceRepository pqServiceRepository) {
		this.payStoreValidation = payStoreValidation;
		this.sendMoneyApi = sendMoneyApi;
		this.userApi = userApi;
		this.transactionValidation = transactionValidation;
		this.userSessionRepository = userSessionRepository;
		this.persistingSessionRegistry = persistingSessionRegistry;
		this.pqServiceRepository = pqServiceRepository;
	}
	
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@RequestMapping(value = "/Process", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> payAtStoreProcess(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestHeader(value = "hash", required = true) String hash, @RequestBody PayAtStoreDTO dto,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				PayAtStoreError error = new PayAtStoreError();
				error = payStoreValidation.checkError(dto, "PAS");
				if (error.isValid()) {
					String sessionId = dto.getSessionId();
					UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
					if (userSession != null) {
						UserDTO user = userApi.getUserById(userSession.getUser().getId());
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							TransactionError transactionError = transactionValidation
									.validateMerchantTransaction(dto.getAmount(), user.getUsername(), pqServiceRepository.findServiceByCode("PAS"));
							if (transactionError.isValid()) {
								sendMoneyApi.sendMoneyStore(dto, user.getUsername(), pqServiceRepository.findServiceByCode("PAS"));
								result.setStatus(ResponseStatus.SUCCESS);
								result.setMessage("Pay at store successful. Money Sent to " + dto.getServiceProvider());
								result.setDetails("Pay at store successful. Money Sent to " + dto.getServiceProvider());
								User u = userApi.findByUserName(user.getUsername());
								result.setBalance(u.getAccountDetail().getBalance());
							} else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Failed to pay at store. Please try again.");
								result.setDetails(transactionError);
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
