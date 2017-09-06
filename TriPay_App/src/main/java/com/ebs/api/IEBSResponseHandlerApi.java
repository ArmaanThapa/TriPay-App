package com.ebs.api;

import com.ebs.model.EBSRedirectResponse;

public interface IEBSResponseHandlerApi {

	EBSRedirectResponse response(String encrypted);
	
}
