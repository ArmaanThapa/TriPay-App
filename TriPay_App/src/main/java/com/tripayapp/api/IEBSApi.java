package com.tripayapp.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.ebs.model.EBSRedirectResponse;
import com.ebs.model.EBSRequest;
import com.tripayapp.entity.PQService;
import com.tripayapp.model.mobile.ResponseDTO;

public interface IEBSApi {

	EBSRequest  requestHandler(EBSRequest request, String username, PQService service);
	ModelAndView responseHandlerView(HttpServletRequest request, String username);
	ResponseDTO responseHandler(EBSRedirectResponse redirectResponse, String username);
	
}
