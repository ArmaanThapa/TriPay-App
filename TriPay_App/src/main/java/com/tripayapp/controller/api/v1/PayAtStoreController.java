package com.tripayapp.controller.api.v1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tripayapp.model.PayStoreDTO;
import com.tripayapp.util.ConvertUtil;
import com.thirdparty.api.IMerchantApi;
import com.thirdparty.model.request.AuthenticationDTO;
import com.thirdparty.model.request.PaymentDTO;
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

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}/PayAtStore")
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
	private final IMerchantApi merchantApi;

	public PayAtStoreController(PayStoreValidation payStoreValidation, ISendMoneyApi sendMoneyApi, IUserApi userApi,
			TransactionValidation transactionValidation, UserSessionRepository userSessionRepository,
			PersistingSessionRegistry persistingSessionRegistry, PQServiceRepository pqServiceRepository,IMerchantApi merchantApi) {
		this.payStoreValidation = payStoreValidation;
		this.sendMoneyApi = sendMoneyApi;
		this.userApi = userApi;
		this.transactionValidation = transactionValidation;
		this.userSessionRepository = userSessionRepository;
		this.persistingSessionRegistry = persistingSessionRegistry;
		this.pqServiceRepository = pqServiceRepository;
		this.merchantApi = merchantApi;
	}
	
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@RequestMapping(value = "/Process", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> payAtStoreProcess(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestHeader(value = "hash", required = true) String hash, @RequestBody PayStoreDTO dto,
			HttpServletRequest request, HttpServletResponse response) {
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
									TransactionError transactionError = transactionValidation
											.validateMerchantTransaction(String.valueOf(dto.getNetAmount()), user.getUsername(), pqServiceRepository.findServiceByCode("PAS"));
									if (transactionError.isValid()) {
										User u = userApi.findByUserName(user.getUsername());
										result = sendMoneyApi.preparePayStore(dto,u);
										result.setBalance(userApi.getWalletBalance(u));
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

}
