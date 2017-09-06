package com.thirdparty.api;

import com.tripayapp.entity.PGDetails;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.TPTransaction;
import com.tripayapp.entity.User;
import com.tripayapp.model.mobile.ResponseDTO;
import com.thirdparty.model.request.AuthenticationDTO;
import com.thirdparty.model.request.PaymentDTO;

public interface IMerchantApi {
    ResponseDTO authenticateMerchant(AuthenticationDTO dto,User u);
    ResponseDTO authenticatePayment(PaymentDTO dto,User u,User m);
    PGDetails getDetailsByMerchant(User u);
    String getTokenByMerchant(User u);
    PQService getServiceByMerchant(User merchant);
    boolean containsValidSecretKey(User merchant,String key);
    TPTransaction getByTransactionRefNo(User merchant, String transactionRefNo);
}
