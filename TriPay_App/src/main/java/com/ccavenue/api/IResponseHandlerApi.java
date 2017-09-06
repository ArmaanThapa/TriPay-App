package com.ccavenue.api;

import com.ccavenue.model.CCAvenueResponse;

public interface IResponseHandlerApi {

	CCAvenueResponse response(String encrypted);
}
