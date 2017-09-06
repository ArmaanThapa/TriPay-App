package com.tripayapp.api.impl;

import com.tripayapp.api.ITransactionApi;
import com.tripayapp.api.IVNetApi;
import com.tripayapp.entity.PQService;
import com.tripayapp.model.VNetDTO;
import com.tripayapp.model.VNetResponse;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VNetApi implements IVNetApi{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ITransactionApi transactionApi;

    public VNetApi(ITransactionApi transactionApi) {
        this.transactionApi = transactionApi;
    }

    @Override
    public VNetDTO processRequest(VNetDTO dto,String username,PQService service) {
        String transactionRefNo = ""+System.currentTimeMillis();
        VNetDTO newDTO = ConvertUtil.convertVNet(dto,transactionRefNo);
        String description = "Load Money through V-Net Banking of "+username;
        transactionApi.initiateLoadMoney(Double.parseDouble(dto.getAmount()),description,service,transactionRefNo,username,newDTO.toJSON().toString());
      return newDTO;
    }

    @Override
    public ResponseDTO handleResponse(VNetResponse dto) {
        ResponseDTO result = new ResponseDTO();
        String paid = dto.getPaid();
        if(paid.equalsIgnoreCase("Y")){
            transactionApi.successLoadMoney(dto.getPrn());
            result.setStatus(ResponseStatus.SUCCESS);
            result.setMessage("Load Money Successful with Bank Reference No. :"+dto.getBid());
        }else{
            transactionApi.failedLoadMoney(dto.getPrn());
            result.setStatus(ResponseStatus.FAILURE);
            result.setMessage("Load Money failed");
        }
        return result;
    }
}
