package com.thirdparty.util;

import com.tripayapp.entity.PGDetails;
import com.tripayapp.entity.TPTransaction;
import com.thirdparty.model.request.AuthenticationDTO;
import com.thirdparty.model.request.StatusResponse;

import java.text.SimpleDateFormat;

public class ConvertUtil {
    public static String convertMerchantDetail(PGDetails pgDetails){
        return pgDetails.getToken()+"|"+pgDetails.getSuccessURL()+"|"+pgDetails.getReturnURL();
    }
    public static StatusResponse convertTransaction(TPTransaction tpTransaction){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setAmount(String.valueOf(tpTransaction.getAmount()));
        statusResponse.setTransactionDate(sdf.format(tpTransaction.getCreated()));
        statusResponse.setPaymentId(tpTransaction.getTransactionRefNo());
        statusResponse.setStatus(tpTransaction.getStatus());
        statusResponse.setMerchantRefNo(tpTransaction.getOrderId());
        return statusResponse;
    }
}
