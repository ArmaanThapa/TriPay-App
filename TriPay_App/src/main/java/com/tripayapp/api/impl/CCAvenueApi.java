package com.tripayapp.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.web.servlet.ModelAndView;

import com.ccavenue.api.IRequestHandlerApi;
import com.ccavenue.api.IResponseHandlerApi;
import com.ccavenue.model.CCAvenueRequest;
import com.ccavenue.model.CCAvenueResponse;
import com.ccavenue.util.CCAvenueConstants;
import com.tripayapp.api.ICCAvenueApi;
import com.tripayapp.api.ITransactionApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.UserDTO;
import com.tripayapp.repositories.PQServiceRepository;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.ModelMapKey;

public class CCAvenueApi implements ICCAvenueApi, MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MessageSource messageSource;

	private static final String SERVICE_CODE = "LMC";

	private final IRequestHandlerApi requestHandlerApi;
	private final IResponseHandlerApi responseHandlerApi;
	private final ITransactionApi transactionApi;
	private final PQServiceRepository pqServiceRepository;
	private final UserSessionRepository userSessionRepository;
	private final IUserApi userApi;

	public CCAvenueApi(IRequestHandlerApi requestHandlerApi, IResponseHandlerApi responseHandlerApi,
			ITransactionApi transactionApi, PQServiceRepository pqServiceRepository,
			UserSessionRepository userSessionRepository, IUserApi userApi) {
		this.requestHandlerApi = requestHandlerApi;
		this.responseHandlerApi = responseHandlerApi;
		this.transactionApi = transactionApi;
		this.pqServiceRepository = pqServiceRepository;
		this.userSessionRepository = userSessionRepository;
		this.userApi = userApi;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public ModelAndView requestHandler(CCAvenueRequest request, ModelAndView modelMap, String username) {
		CCAvenueRequest ccAvenueRequest = requestHandlerApi.request(request);
		transactionApi.initiateLoadMoney(Double.parseDouble(ccAvenueRequest.getAmount()), "Load Money",
				pqServiceRepository.findServiceByCode(SERVICE_CODE), ccAvenueRequest.getOrderId(), username, request.toJSON().toString());
		modelMap.addObject("encRequest", ccAvenueRequest.getEncryptedResponse());
		modelMap.addObject("access_code", CCAvenueConstants.ACCESS_CODE);
		modelMap.setViewName(
				"redirect:https://secure.ccavenue.com/transaction/transaction.do?command=initiateTransaction");
		return modelMap;
	}

	@Override
	public ModelAndView responseHandler(String encryptedResponse, ModelAndView modelMap, String username) {
		CCAvenueResponse response = responseHandlerApi.response(encryptedResponse);
		if (response.getOrderStatus().equals("Success")) {
			transactionApi.successLoadMoney(response.getOrderId());
		} else {
			transactionApi.failedLoadMoney(response.getOrderId());
		}
		modelMap.addObject(ModelMapKey.MESSAGE, "Load money " + response.getOrderStatus() + ".");
		modelMap.setViewName("redirect:/User/LoadMoney");
		return modelMap;
	}

	@Override
	public String responseHandlerMobile(String encryptedResponse) {
		CCAvenueResponse response = responseHandlerApi.response(encryptedResponse);
		String sessionId = response.getMerchantParam2();
		String status = "";
		UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
		if (userSession != null) {
			UserDTO user = userApi.getUserById(userSession.getUser().getId());
			if (user.getAuthority().contains(Authorities.USER)
					&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
				if (response.getOrderStatus().equals("Success")) {
					transactionApi.successLoadMoney(response.getOrderId());
				} else {
					transactionApi.failedLoadMoney(response.getOrderId());
				}
				status = response.getOrderStatus();
			} else {
				status = "Permission Not Granted";
			}
		} else {
			status = "Invalid Session";
		}
		return status;
	}

	@Override
	public String requestRSAKey(String accessCode, String orderId) {
		String rsaKey = requestHandlerApi.requestRSAKey(accessCode, orderId);
		return rsaKey;

	}

}
