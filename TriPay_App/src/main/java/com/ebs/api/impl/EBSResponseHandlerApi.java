package com.ebs.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ebs.api.IEBSResponseHandlerApi;
import com.ebs.model.EBSRedirectResponse;

public class EBSResponseHandlerApi implements IEBSResponseHandlerApi {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public EBSRedirectResponse response(String encrypted) {
		return null;
	}

}
