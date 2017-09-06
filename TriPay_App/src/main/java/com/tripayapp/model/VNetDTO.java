package com.tripayapp.model;

import com.thirdparty.model.request.JSONWrapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class VNetDTO extends SessionDTO implements JSONWrapper{
    private String pid;
    private String amount;
    private String merchantName;
    private String mid;
    private String itc;
    private String crnNo;
    private String prnNo;
    private String returnURL;
    private String serviceCode;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getItc() {
        return itc;
    }

    public void setItc(String itc) {
        this.itc = itc;
    }

    public String getCrnNo() {
        return crnNo;
    }

    public void setCrnNo(String crnNo) {
        this.crnNo = crnNo;
    }

    public String getPrnNo() {
        return prnNo;
    }

    public void setPrnNo(String prnNo) {
        this.prnNo = prnNo;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    /**
     *     private String pid;
     private String amount;
     private String merchantName;
     private String mid;
     private String itc;
     private String crnNo;
     private String prnNo;
     private String returnURL;
     private String serviceCode;

     * @return
     */
    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("pid", getPid());
            json.put("amount",getAmount());
            json.put("merchantName",getMerchantName());
            json.put("mid",getMid());
            json.put("itc",getItc());
            json.put("crnNo",getCrnNo());
            json.put("prnNo",getPrnNo());
            json.put("returnURL",getReturnURL());
            json.put("serviceCode",getServiceCode());
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        return json;
    }
}
