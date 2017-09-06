package com.thirdparty.api.impl;

import com.tripayapp.api.ITransactionApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.*;
import com.tripayapp.model.Status;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.MRequestLogRepository;
import com.tripayapp.repositories.PGDetailsRepository;
import com.tripayapp.repositories.PQServiceRepository;
import com.tripayapp.repositories.TPTransactionRepository;
import com.tripayapp.util.SecurityUtil;
import com.thirdparty.api.IMerchantApi;
import com.thirdparty.model.request.AuthenticationDTO;
import com.thirdparty.model.request.AuthenticationResponse;
import com.thirdparty.model.request.PaymentDTO;
import com.thirdparty.util.ConvertUtil;

import java.util.List;

public class MerchantApi implements IMerchantApi{

    private final IUserApi userApi;
    private final ITransactionApi transactionApi;
    private final PQServiceRepository pqServiceRepository;
    private final MRequestLogRepository mRequestLogRepository;
    private final PGDetailsRepository pgDetailsRepository;
    private final TPTransactionRepository tpTransactionRepository;

    public MerchantApi(IUserApi userApi, ITransactionApi transactionApi, PQServiceRepository pqServiceRepository, MRequestLogRepository mRequestLogRepository, PGDetailsRepository pgDetailsRepository, TPTransactionRepository tpTransactionRepository) {
        this.userApi = userApi;
        this.transactionApi = transactionApi;
        this.pqServiceRepository = pqServiceRepository;
        this.mRequestLogRepository = mRequestLogRepository;
        this.pgDetailsRepository = pgDetailsRepository;
        this.tpTransactionRepository = tpTransactionRepository;
    }

    @Override
    public ResponseDTO authenticateMerchant(AuthenticationDTO dto,User u){
        ResponseDTO response = new ResponseDTO();
        PGDetails pgDetails = pgDetailsRepository.findByUser(u);
        if(pgDetails != null) {
            boolean isPG = pgDetails.isPaymentGateway();
            if(isPG) {
                TPTransaction tpTransaction = tpTransactionRepository.findByOrderIdAndMerchant(dto.getTransactionID(), u);
                if (tpTransaction == null) {
                    tpTransaction = new TPTransaction();
                    tpTransaction.setMerchant(pgDetails.getUser());
                    tpTransaction.setStatus(Status.Processing);
                    tpTransaction.setAmount(Double.parseDouble(dto.getAmount()));
                    tpTransaction.setOrderId(dto.getTransactionID());
                    tpTransactionRepository.save(tpTransaction);
                    MRequestLog log = new MRequestLog();
                    log.setPgDetails(pgDetails);
                    log.setRequest(String.valueOf(dto.toJSON()));
                    AuthenticationResponse authenticationResponse = new AuthenticationResponse();
                    authenticationResponse.setUsername(u.getUsername());
                    authenticationResponse.setSuccessURL(pgDetails.getSuccessURL());
                    authenticationResponse.setFailureURL(pgDetails.getReturnURL());
                    authenticationResponse.setImage(u.getUserDetail().getImage());
                    authenticationResponse.setMerchantId(u.getId());
                    response.setStatus(ResponseStatus.SUCCESS);
                    response.setMessage("Valid Merchant");
                    response.setDetails(authenticationResponse);
                    log.setStatus(Status.Success);
                    mRequestLogRepository.save(log);
                } else {
                    response.setStatus(ResponseStatus.FAILURE);
                    response.setMessage("Transaction Already Exists");
                }
            }else {
                response.setStatus(ResponseStatus.FAILURE);
                response.setMessage("Not Authorized to use PG");
            }
        }else {
            response.setStatus(ResponseStatus.FAILURE);
            response.setMessage("Not a Valid Merchant");
        }
    return response;
    }

    @Override
    public ResponseDTO authenticatePayment(PaymentDTO dto,User u,User m) {
        ResponseDTO response = new ResponseDTO();
        PQService merchantService = null;
        PGDetails pgDetails = pgDetailsRepository.findByUser(m);
        if(pgDetails != null){
            String transactionRefNo = ""+System.currentTimeMillis();
            String name = m.getUserDetail().getFirstName()+" "+m.getUserDetail().getLastName();
            String description = "Payment Of "+dto.getNetAmount()+" to "+name;
            TPTransaction tpTransaction = tpTransactionRepository.findByOrderId(dto.getTransactionID());
            if(tpTransaction != null){
                merchantService = pgDetails.getService();
                Status status = tpTransaction.getStatus();
                if(status.equals(Status.Processing)) {
                    tpTransaction.setTransactionRefNo(transactionRefNo);
                    tpTransaction.setUser(u);
                    transactionApi.initiateMerchantPayment(dto.getNetAmount(), description, merchantService, transactionRefNo, u.getUsername(), m.getUsername(), null,true);
                    transactionApi.successMerchantPayment(transactionRefNo,true);
                    tpTransaction.setStatus(Status.Success);
                    tpTransactionRepository.save(tpTransaction);
                    response.setStatus(ResponseStatus.SUCCESS);
                    response.setMessage("Payment Successful");
                    response.setDetails("Your Payment is Successful your "+m.getUserDetail().getFirstName()+ " transactionID is "+dto.getTransactionID()+" and VPayQwik Transaction ID is "+transactionRefNo);
                }else{
                  response.setStatus(ResponseStatus.FAILURE);
                  response.setMessage("Transaction is already Processed");
                }
            }else {
                response.setStatus(ResponseStatus.FAILURE);
                response.setMessage("Transaction Not Exists");
            }
        }else {
            response.setStatus(ResponseStatus.FAILURE);
            response.setMessage("Payment Gateway Not Activated");
        }
        return response;
    }

    @Override
    public PGDetails getDetailsByMerchant(User u) {
        return pgDetailsRepository.findByUser(u);
    }

    @Override
    public String getTokenByMerchant(User u) {
        return pgDetailsRepository.findTokenByMerchant(u);
    }

    @Override
    public PQService getServiceByMerchant(User merchant) {
        return pgDetailsRepository.findServiceByUser(merchant);
    }

    @Override
    public boolean containsValidSecretKey(User merchant,String key) {
        boolean valid = false;
        PGDetails merchantPG = pgDetailsRepository.findByUser(merchant);
        if(merchantPG != null){
            String validKey = merchantPG.getToken();
            if(validKey != null){
                if(validKey.equalsIgnoreCase(key)){
                    valid = true;
                }
            }
        }
        return valid;
    }

    @Override
    public TPTransaction getByTransactionRefNo(User merchant, String transactionRefNo) {
        TPTransaction tpTransaction = tpTransactionRepository.findByOrderIdAndMerchant(transactionRefNo,merchant);
        return tpTransaction;
    }


}
