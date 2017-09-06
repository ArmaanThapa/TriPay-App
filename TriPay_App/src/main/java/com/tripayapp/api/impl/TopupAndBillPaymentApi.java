package com.tripayapp.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import com.instantpay.model.request.TransactionRequest;
import com.instantpay.model.response.TransactionResponse;
import com.instantpay.util.IPayConvertUtil;
import com.instantpay.util.InstantPayConstants;
import com.tripayapp.api.ITopupAndBillPaymentApi;
import com.tripayapp.api.ITransactionApi;
import com.tripayapp.entity.PQService;
import com.tripayapp.model.CallBackRequest;
import com.tripayapp.model.DTHBillPaymentDTO;
import com.tripayapp.model.ElectricityBillPaymentDTO;
import com.tripayapp.model.GasBillPaymentDTO;
import com.tripayapp.model.InsuranceBillPaymentDTO;
import com.tripayapp.model.LandlineBillPaymentDTO;
import com.tripayapp.model.MobileTopupDTO;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import org.springframework.transaction.annotation.Transactional;

public class TopupAndBillPaymentApi implements ITopupAndBillPaymentApi, MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MessageSource messageSource;

	private final com.instantpay.api.ITransactionApi ipayTransactionApi;
	private final com.instantpay.api.IValidationApi ipayValidationApi;
	private final ITransactionApi transactionApi;

	public TopupAndBillPaymentApi(com.instantpay.api.ITransactionApi ipayTransactionApi,
			com.instantpay.api.IValidationApi ipayValidationApi, ITransactionApi transactionApi) {
		this.ipayTransactionApi = ipayTransactionApi;
		this.ipayValidationApi = ipayValidationApi;
		this.transactionApi = transactionApi;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public TransactionResponse prepaidTopup(MobileTopupDTO dto, String username, PQService service) {

		TransactionRequest request = IPayConvertUtil.prepaidRequest(dto);
		String transactionRefNo = System.currentTimeMillis() + "";
		request.setAgentId(transactionRefNo);
		
		transactionApi.initiateBillPayment(Double.parseDouble(dto.getAmount()),
				"Prepaid Topup Rs " + dto.getAmount() + " to " + dto.getMobileNo(),
				service, transactionRefNo, username,
				InstantPayConstants.USERNAME, dto.toJSON().toString());
		if (request.getSpKey().equalsIgnoreCase("VATP")) {
			request.setOptional5(InstantPayConstants.OUTLET_ID);
		}

		TransactionResponse response = ipayTransactionApi.request(request, service);
		if (response.isSuccess()) {
			transactionApi.successBillPayment(transactionRefNo);
		} else {
			transactionApi.failedBillPayment(transactionRefNo);
		}
		return response;
	}

	@Override
	public TransactionResponse postpaidTopup(MobileTopupDTO dto, String username, PQService service) {
		TransactionRequest request = IPayConvertUtil.postpaidRequest(dto);
		String transactionRefNo = System.currentTimeMillis() + "";
		request.setAgentId(transactionRefNo);
		transactionApi.initiateBillPayment(Double.parseDouble(dto.getAmount()),
				"Postpaid Topup Rs " + dto.getAmount() + " to " + dto.getMobileNo(),
				service, transactionRefNo, username,
				InstantPayConstants.USERNAME, dto.toJSON().toString());
		if (request.getSpKey().equalsIgnoreCase("VATP")) {
			request.setOptional5(InstantPayConstants.OUTLET_ID);
		}
		TransactionResponse response = ipayTransactionApi.request(request, service);
		if (response.isSuccess()) {
			transactionApi.successBillPayment(transactionRefNo);
		} else {
			transactionApi.failedBillPayment(transactionRefNo);
		}
		return response;
	}

	@Override
	public TransactionResponse datacardTopup(MobileTopupDTO dto, String username, PQService service) {
		TransactionRequest request = IPayConvertUtil.datacardRequest(dto);
		String transactionRefNo = System.currentTimeMillis() + "";
		request.setAgentId(transactionRefNo);

		transactionApi.initiateBillPayment(Double.parseDouble(dto.getAmount()),
				"DataCard Topup Rs " + dto.getAmount() + " to " + dto.getMobileNo(),
				service, transactionRefNo, username,
				InstantPayConstants.USERNAME, dto.toJSON().toString());
		if (request.getSpKey().equalsIgnoreCase("VATP")) {
			request.setOptional5(InstantPayConstants.OUTLET_ID);
		}
		TransactionResponse response = ipayTransactionApi.request(request, service);
		if (response.isSuccess()) {
			transactionApi.successBillPayment(transactionRefNo);
		} else {
			transactionApi.failedBillPayment(transactionRefNo);
		}
		return response;
	}

	@Override
	public TransactionResponse dthBillPayment(DTHBillPaymentDTO dto, String username, PQService service) {
		TransactionRequest request = IPayConvertUtil.dthRequest(dto);
		String transactionRefNo = System.currentTimeMillis() + "";
		request.setAgentId(transactionRefNo);

		transactionApi.initiateBillPayment(Double.parseDouble(dto.getAmount()),
				"DTH bill payment Rs " + dto.getAmount() + " to " + dto.getDthNo(),
				service, transactionRefNo, username,
				InstantPayConstants.USERNAME, dto.toJSON().toString());
		if (request.getSpKey().equalsIgnoreCase("VATP")) {
			request.setOptional5(InstantPayConstants.OUTLET_ID);
		}
		TransactionResponse response = ipayTransactionApi.request(request, service);
		if (response.isSuccess()) {
			transactionApi.successBillPayment(transactionRefNo);
		} else {
			transactionApi.failedBillPayment(transactionRefNo);
		}
		return response;
	}

	@Override
	public TransactionResponse landlineBillPayment(LandlineBillPaymentDTO dto, String username, PQService service) {
		TransactionRequest request = IPayConvertUtil.landlineRequest(dto);
		String transactionRefNo = System.currentTimeMillis() + "";
		request.setAgentId(transactionRefNo);
		transactionApi.initiateBillPayment(Double.parseDouble(dto.getAmount()),
				"Landline bill payment Rs " + dto.getAmount() + " to " + dto.getStdCode() + dto.getLandlineNumber(),
				service, transactionRefNo, username,
				InstantPayConstants.USERNAME, dto.toJSON().toString());
		if (request.getSpKey().equalsIgnoreCase("VBGL")) {
			request.setOptional2(dto.getAccountNumber());
			request.setOptional3("LLI");
		}
		if (request.getSpKey().equalsIgnoreCase("VATP")) {
			request.setOptional5(InstantPayConstants.OUTLET_ID);
		}

		TransactionResponse response = ipayTransactionApi.request(request, service);
		if (response.isSuccess()) {
			transactionApi.successBillPayment(transactionRefNo);
		} else {
			transactionApi.failedBillPayment(transactionRefNo);
		}
		return response;
	}

	@Override
	public TransactionResponse electricityBillPayment(ElectricityBillPaymentDTO dto, String username, PQService service) {
		TransactionRequest request = IPayConvertUtil.electricityRequest(dto);
		String transactionRefNo = System.currentTimeMillis() + "";
		request.setAgentId(transactionRefNo);
		transactionApi.initiateBillPayment(Double.parseDouble(dto.getAmount()),
				"Electricity bill payment Rs " + dto.getAmount() + " to " + dto.getAccountNumber(),
				service, transactionRefNo, username,
				InstantPayConstants.USERNAME, dto.toJSON().toString());

		TransactionResponse response = ipayTransactionApi.request(request, service);
		if (response.isSuccess()) {
			transactionApi.successBillPayment(transactionRefNo);
		} else {
			transactionApi.failedBillPayment(transactionRefNo);
		}
		return response;
	}

	@Override
	public TransactionResponse gasBillPayment(GasBillPaymentDTO dto, String username, PQService service) {
		TransactionRequest request = IPayConvertUtil.gasRequest(dto);
		String transactionRefNo = System.currentTimeMillis() + "";
		request.setAgentId(transactionRefNo);
		transactionApi.initiateBillPayment(Double.parseDouble(dto.getAmount()),
				"Gas bill payment Rs " + dto.getAmount() + " to " + dto.getAccountNumber(),
				service, transactionRefNo, username,
				InstantPayConstants.USERNAME, dto.toJSON().toString());
		TransactionResponse response = ipayTransactionApi.request(request, service);
		if (response.isSuccess()) {
			transactionApi.successBillPayment(transactionRefNo);
		} else {
			transactionApi.failedBillPayment(transactionRefNo);
		}
		return response;
	}

	@Override
	public TransactionResponse insuranceBillPayment(InsuranceBillPaymentDTO dto, String username, PQService service) {
		TransactionRequest request = IPayConvertUtil.insuranceRequest(dto);
		String transactionRefNo = System.currentTimeMillis() + "";
		request.setAgentId(transactionRefNo);
		transactionApi.initiateBillPayment(Double.parseDouble(dto.getAmount()),
				"Insurance bill payment Rs " + dto.getAmount() + " to " + dto.getPolicyNumber(),
				service, transactionRefNo, username,
				InstantPayConstants.USERNAME, dto.toJSON().toString());
		TransactionResponse response = ipayTransactionApi.request(request, service);
		if (response.isSuccess()) {
			transactionApi.successBillPayment(transactionRefNo);
		} else {
			transactionApi.failedBillPayment(transactionRefNo);
		}
		return response;
	}

	@Override
	public ResponseDTO callbackHandler(CallBackRequest dto) {
		ResponseDTO result = new ResponseDTO();
		if (dto.getRes_code().equalsIgnoreCase("TXN")) {
			transactionApi.successBillPayment(dto.getAgent_id());
			result.setStatus(ResponseStatus.SUCCESS);
			result.setMessage("Transaction successful.");
		} else {
			transactionApi.failedBillPayment(dto.getAgent_id());
			result.setStatus(ResponseStatus.FAILURE);
			result.setMessage("Failed, Please try again later.");
		}
		return result;
	}

}
