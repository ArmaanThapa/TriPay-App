package com.ebs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ebs.model.EBSRequest;
import com.ebs.util.EBSConstants;
import com.tripayapp.api.IEBSApi;

@Controller
@RequestMapping("/EBSGateway/Test/")
public class TestController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final IEBSApi ebsApi;

	public TestController(IEBSApi ebsApi) {
		this.ebsApi = ebsApi;
	}

	@RequestMapping
	public String getFormPage(Model model) {
		EBSRequest request = new EBSRequest();
		model.addAttribute("ebs", request);
		return "EBSLoadMoney";
	}

	@RequestMapping(value = "/Process", method = RequestMethod.POST)
	public String processPayment(@ModelAttribute("ebs") EBSRequest request, Model model) {
		System.out.println(request);
//		EBSRequest mav = ebsApi.requestHandler(request, "1000000002");
//		model.addAttribute("pay", mav);
		model.addAttribute("url", EBSConstants.URL_INIT_TRANSACTION);
		return "LoadMoneyProcess";
	}
}
