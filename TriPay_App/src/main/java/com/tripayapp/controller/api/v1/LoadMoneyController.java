package com.tripayapp.controller.api.v1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tripayapp.api.IVNetApi;
import com.tripayapp.model.VNetDTO;
import com.tripayapp.model.VNetError;
import com.tripayapp.model.VNetResponse;
import com.tripayapp.validation.VNetValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ebs.model.EBSRedirectResponse;
import com.ebs.model.EBSRequest;
import com.ebs.model.error.EBSRequestError;
import com.ebs.validation.EBSValidation;
import com.tripayapp.api.IEBSApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.error.TransactionError;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.PQServiceRepository;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.validation.TransactionValidation;

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}/LoadMoney")
public class LoadMoneyController implements MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MessageSource messageSource;

	private final IEBSApi ebsApi;
	private final EBSValidation ebsValidation;
	private final IVNetApi vNetApi;
	private final IUserApi userApi;
	private final TransactionValidation transactionValidation;
	private final UserSessionRepository userSessionRepository;
	private final PersistingSessionRegistry persistingSessionRegistry;
	private final PQServiceRepository pqServiceRepository;
	private final VNetValidation vNetValidation;
	public LoadMoneyController(IEBSApi ebsApi, EBSValidation ebsValidation, UserSessionRepository userSessionRepository,
			PersistingSessionRegistry persistingSessionRegistry, IUserApi userApi,
			TransactionValidation transactionValidation, PQServiceRepository pqServiceRepository,VNetValidation vNetValidation,IVNetApi vNetApi) {
		this.ebsApi = ebsApi;
		this.ebsValidation = ebsValidation;
		this.userSessionRepository = userSessionRepository;
		this.persistingSessionRegistry = persistingSessionRegistry;
		this.userApi = userApi;
		this.transactionValidation = transactionValidation;
		this.pqServiceRepository = pqServiceRepository;
		this.vNetValidation = vNetValidation;
		this.vNetApi = vNetApi;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@RequestMapping(value = "/InitiateVNet", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ResponseDTO> initiateVNetBanking(@PathVariable(value = "role") String role,
														   @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
														   @RequestBody VNetDTO dto, HttpServletRequest request, HttpServletResponse response,
														   Model modelMap) {
		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("User")) {
			String sessionId = dto.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			VNetError  error = vNetValidation.validateRequest(dto);
			if (error.isValid()) {
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							PQService service = pqServiceRepository.findServiceByCode("LMB");
							TransactionError transactionError = transactionValidation
									.validateLoadMoneyTransaction(dto.getAmount(), user.getUsername(), service);
							logger.info(transactionError.getMessage());
							if (transactionError.isValid()) {
								VNetDTO newDTO = vNetApi.processRequest(dto,user.getUsername(),service);
                                result.setStatus(ResponseStatus.SUCCESS);
								result.setMessage("Success");
								result.setDetails(newDTO);
								return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
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
			logger.info("ROLE Error");
			result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
			result.setMessage("Failed, Unauthorized user.");
			result.setDetails("Failed, Unauthorized user.");
		}
		modelMap.addAttribute("error", "FAILED");
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}


	@RequestMapping(value = "/RedirectVNet", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ResponseDTO> redirectLoadMoneyVNet(@PathVariable(value = "role") String role,
															 @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
															 @RequestBody VNetResponse dto, HttpServletRequest request,
															 HttpServletResponse response, Model model) {
		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("User") && device.equalsIgnoreCase("Website")) {
			result = vNetApi.handleResponse(dto);
			return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}


	@RequestMapping(value = "/Process", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ResponseDTO> processLoadMoney(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody EBSRequest ebsRequest, HttpServletRequest request, HttpServletResponse response,
			Model modelMap) {
		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("User")) {
			String sessionId = ebsRequest.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			EBSRequestError error = new EBSRequestError();
			error = ebsValidation.validateRequest(ebsRequest, "LMC");
			if (error.isValid()) {
				logger.info("Error is valid");
				if (userSession != null) {
					logger.info("Session is not null");
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						logger.info("User is not null");
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							PQService service = pqServiceRepository.findServiceByCode("LMC");
							TransactionError transactionError = transactionValidation
									.validateLoadMoneyTransaction(ebsRequest.getAmount(), user.getUsername(), service);
							logger.info(transactionError.getMessage());
							if (transactionError.isValid()) {
								logger.info("Transaction Error is valid");
								EBSRequest newRequest = ebsApi.requestHandler(ebsRequest, user.getUsername(), service);
								result.setStatus(ResponseStatus.SUCCESS);
								result.setMessage("Success");
								result.setDetails(newRequest);
								return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
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
			logger.info("ROLE Error");
			result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
			result.setMessage("Failed, Unauthorized user.");
			result.setDetails("Failed, Unauthorized user.");
		}
		modelMap.addAttribute("error", "FAILED");
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/Redirect", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ResponseDTO> redirectLoadMoney(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@ModelAttribute EBSRedirectResponse redirectResponse, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("User") && device.equalsIgnoreCase("Website")) {
			result = ebsApi.responseHandler(redirectResponse, null);
			return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}
}
