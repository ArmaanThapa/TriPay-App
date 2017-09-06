package com.tripayapp.api;

import com.tripayapp.entity.PQService;
import com.tripayapp.entity.User;
import com.tripayapp.model.*;
import com.tripayapp.model.mobile.ResponseDTO;

public interface ISendMoneyApi {

	String sendMoneyMobile(SendMoneyMobileDTO dto, String username, PQService service);

	String sendMoneyBankInitiate(SendMoneyBankDTO dto, String username, PQService service);

	String sendMoneyBankFailed(String transactionRefNo);

	String sendMoneyBankSuccess(String transactionRefNo);

	String sendMoneyStore(PayAtStoreDTO dto, String username, PQService service);

	String prepareSendMoney(String username);

	ResponseDTO preparePayStore(PayStoreDTO dto,User u);

	ResponseDTO refundMoneyToAccount(RefundDTO dto, User u);

	void refundMoney(RefundDTO dto,User u,PQService service);
	
	SendMoneyResponse checkInputParameters (SendMoneyBankDTO dto);
	
	SendMoneyResponse sendMoneyMBankInitiate(SendMoneyBankDTO dto, String username, PQService service);

}
