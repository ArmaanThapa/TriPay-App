package com.tripayapp.controller.api.v1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.tripayapp.api.ITopupAndBillPaymentApi;
import com.tripayapp.model.CallBackRequest;
import com.tripayapp.model.mobile.ResponseDTO;

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}")
public class CallBackController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final ITopupAndBillPaymentApi topupAndBillPaymentApi;

	public CallBackController(ITopupAndBillPaymentApi topupAndBillPaymentApi) {
		this.topupAndBillPaymentApi = topupAndBillPaymentApi;
	}

	@RequestMapping(value = "/InstantPay/Callback", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> callBackController(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody CallBackRequest dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		return new ResponseEntity<ResponseDTO>(topupAndBillPaymentApi.callbackHandler(dto), HttpStatus.OK);
	}

}
