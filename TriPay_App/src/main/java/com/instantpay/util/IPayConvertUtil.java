package com.instantpay.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.instantpay.model.request.TransactionRequest;
import com.tripayapp.model.DTHBillPaymentDTO;
import com.tripayapp.model.ElectricityBillPaymentDTO;
import com.tripayapp.model.GasBillPaymentDTO;
import com.tripayapp.model.InsuranceBillPaymentDTO;
import com.tripayapp.model.LandlineBillPaymentDTO;
import com.tripayapp.model.MobileTopupDTO;

public class IPayConvertUtil {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static TransactionRequest prepaidRequest(MobileTopupDTO dto) {
		TransactionRequest request = new TransactionRequest();
		request.setAccount(dto.getMobileNo());
		request.setAmount(dto.getAmount());
		request.setSpKey(dto.getServiceProvider());
		if (dto.getServiceProvider().equalsIgnoreCase("ATP")) {
			request.setOptional5(InstantPayConstants.OUTLET_ID);
		}
		request.setToken(InstantPayConstants.TOKEN);
		request.setFormat(InstantPayConstants.REQUEST_FORMAT);
		String transactionReferenceNumber = System.currentTimeMillis() + "";
		request.setAgentId(transactionReferenceNumber);
		return request;
	}

	public static TransactionRequest postpaidRequest(MobileTopupDTO dto) {
		TransactionRequest request = new TransactionRequest();
		request.setAccount(dto.getMobileNo());
		request.setAmount(dto.getAmount());
		request.setSpKey(dto.getServiceProvider());
		request.setToken(InstantPayConstants.TOKEN);
		request.setFormat(InstantPayConstants.REQUEST_FORMAT);
		request.setAgentId(InstantPayConstants.AGENT_ID);
		return request;
	}

	public static TransactionRequest datacardRequest(MobileTopupDTO dto) {
		TransactionRequest request = new TransactionRequest();
		request.setAccount(dto.getMobileNo());
		request.setAmount(dto.getAmount());
		request.setSpKey(dto.getServiceProvider());
		if (dto.getServiceProvider().equalsIgnoreCase("ATP")) {
			request.setOptional5(InstantPayConstants.AGENT_ID);
		}
		request.setToken(InstantPayConstants.TOKEN);
		request.setFormat(InstantPayConstants.REQUEST_FORMAT);
		request.setAgentId(InstantPayConstants.AGENT_ID);

		return request;
	}

	public static TransactionRequest dthRequest(DTHBillPaymentDTO dto) {
		TransactionRequest request = new TransactionRequest();
		request.setAccount(dto.getDthNo());
		request.setAmount(dto.getAmount());
		request.setSpKey(dto.getServiceProvider());
		request.setToken(InstantPayConstants.TOKEN);
		request.setFormat(InstantPayConstants.REQUEST_FORMAT);
		request.setAgentId(InstantPayConstants.AGENT_ID);
		return request;
	}

	public static TransactionRequest landlineRequest(LandlineBillPaymentDTO dto) {
		TransactionRequest request = new TransactionRequest();
		request.setAccount(dto.getLandlineNumber());
		request.setAmount(dto.getAmount());
		request.setSpKey(dto.getServiceProvider());
		request.setOptional1(dto.getStdCode());
		if (dto.getServiceProvider().equalsIgnoreCase("BGL")) {
			request.setOptional2(dto.getAccountNumber());
			request.setOptional3("LLI");
		}
		request.setToken(InstantPayConstants.TOKEN);
		request.setAgentId(InstantPayConstants.AGENT_ID);
		request.setFormat(InstantPayConstants.REQUEST_FORMAT);
		return request;
	}

	public static TransactionRequest electricityRequest(ElectricityBillPaymentDTO dto) {
		TransactionRequest request = new TransactionRequest();
		request.setAccount(dto.getAccountNumber());
		request.setAmount(dto.getAmount());
		request.setSpKey(dto.getServiceProvider());
		System.err.println(dto.getServiceProvider());
		if (dto.getServiceProvider().equalsIgnoreCase("VREE")) {
			request.setOptional1(dto.getCycleNumber());
		}
		request.setToken(InstantPayConstants.TOKEN);
		request.setAgentId(InstantPayConstants.AGENT_ID);
		request.setFormat(InstantPayConstants.REQUEST_FORMAT);
		return request;
	}

	public static TransactionRequest gasRequest(GasBillPaymentDTO dto) {
		TransactionRequest request = new TransactionRequest();
		request.setAccount(dto.getAccountNumber());
		request.setAmount(dto.getAmount());
		request.setSpKey(dto.getServiceProvider());
		request.setToken(InstantPayConstants.TOKEN);
		request.setAgentId(InstantPayConstants.AGENT_ID);
		request.setFormat(InstantPayConstants.REQUEST_FORMAT);
		return request;
	}

	public static TransactionRequest insuranceRequest(InsuranceBillPaymentDTO dto) {
		TransactionRequest request = new TransactionRequest();
		request.setAccount(dto.getPolicyNumber());
		request.setAmount(dto.getAmount());
		request.setSpKey(dto.getServiceProvider());
		request.setOptional1(dto.getPolicyDate());
		request.setToken(InstantPayConstants.TOKEN);
		request.setAgentId(InstantPayConstants.AGENT_ID);
		request.setFormat(InstantPayConstants.REQUEST_FORMAT);
		return request;
	}
}
