package com.tripayapp.api;

import com.instantpay.model.response.TransactionResponse;
import com.tripayapp.entity.PQService;
import com.tripayapp.model.CallBackRequest;
import com.tripayapp.model.DTHBillPaymentDTO;
import com.tripayapp.model.ElectricityBillPaymentDTO;
import com.tripayapp.model.GasBillPaymentDTO;
import com.tripayapp.model.InsuranceBillPaymentDTO;
import com.tripayapp.model.LandlineBillPaymentDTO;
import com.tripayapp.model.MobileTopupDTO;
import com.tripayapp.model.mobile.ResponseDTO;

public interface ITopupAndBillPaymentApi {

	TransactionResponse prepaidTopup(MobileTopupDTO dto, String username, PQService service);

	TransactionResponse postpaidTopup(MobileTopupDTO dto, String username, PQService service);

	TransactionResponse datacardTopup(MobileTopupDTO dto, String username, PQService service);

	TransactionResponse dthBillPayment(DTHBillPaymentDTO dto, String username, PQService service);

	TransactionResponse landlineBillPayment(LandlineBillPaymentDTO dto, String username, PQService service);

	TransactionResponse electricityBillPayment(ElectricityBillPaymentDTO dto, String username, PQService service);

	TransactionResponse gasBillPayment(GasBillPaymentDTO dto, String username, PQService service);

	TransactionResponse insuranceBillPayment(InsuranceBillPaymentDTO dto, String username, PQService service);
	
	ResponseDTO callbackHandler(CallBackRequest dto);

}
