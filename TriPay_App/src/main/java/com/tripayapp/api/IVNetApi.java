package com.tripayapp.api;

import com.tripayapp.entity.PQService;
import com.tripayapp.model.VNetDTO;
import com.tripayapp.model.VNetResponse;
import com.tripayapp.model.mobile.ResponseDTO;

public interface IVNetApi {
    VNetDTO processRequest(VNetDTO dto, String username, PQService service);

    ResponseDTO handleResponse(VNetResponse dto);
}
