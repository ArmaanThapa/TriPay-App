package com.tripayapp.api.impl;

import java.util.ArrayList;
import java.util.List;

import com.tripayapp.api.ICommissionApi;
import com.tripayapp.entity.*;
import com.tripayapp.model.*;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.*;
import com.tripayapp.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripayapp.api.IMerchantApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.util.ConvertUtil;

public class MerchantApi implements IMerchantApi {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final IUserApi userApi;
	private final UserRepository userRepository;
	private final PGDetailsRepository pgDetailsRepository;
	private final PQServiceRepository pqServiceRepository;
	private final PQServiceTypeRepository pqServiceTypeRepository;
	private final PQOperatorRepository pqOperatorRepository;
	private final ICommissionApi commissionApi;
	public MerchantApi(IUserApi userApi, UserRepository userRepository, PGDetailsRepository pgDetailsRepository, PQServiceRepository pqServiceRepository, PQServiceTypeRepository pqServiceTypeRepository, PQOperatorRepository pqOperatorRepository,ICommissionApi commissionApi) {
		this.userApi = userApi;
		this.userRepository = userRepository;
		this.pgDetailsRepository = pgDetailsRepository;
		this.pqServiceRepository = pqServiceRepository;
		this.pqServiceTypeRepository = pqServiceTypeRepository;
		this.pqOperatorRepository = pqOperatorRepository;
		this.commissionApi = commissionApi;
	}

	@Override
	public ResponseDTO addMerchant(MRegisterDTO user) {
		ResponseDTO result = new ResponseDTO();
		user.setUserType(UserType.Merchant);
		String firstName = user.getFirstName();
		int length = firstName.length();
		int i = 1;
		String serviceCode = (firstName.charAt(0)+""+firstName.charAt(1)+""+firstName.charAt(length-1)).toUpperCase();
		PQService exists  = pqServiceRepository.findServiceByOperatorCode(serviceCode);
		while((exists != null) && (i <= length)){
			serviceCode = (serviceCode+firstName.charAt(i)).toUpperCase();
			exists  = pqServiceRepository.findServiceByOperatorCode(serviceCode);
			i++;
		}

		try {
			userApi.saveMerchant(user);
			PQService service = new PQService();
			PQOperator operator = pqOperatorRepository.findOperatorByName("VPayQwik");

			PQServiceType serviceType = pqServiceTypeRepository.findServiceTypeByName(user.getServiceName());
			if(serviceType != null){
				service.setServiceType(serviceType);
			}
			if(operator != null){
				service.setOperator(operator);
			}

			service.setCode("V"+serviceCode);
			service.setStatus(Status.Active);
			service.setOperatorCode(serviceCode);
			service.setName(firstName+" Payment");
			service.setDescription("Payment to "+firstName);
			service.setMinAmount(1);
			service.setMaxAmount(100000);
			pqServiceRepository.save(service);

			PQCommission commission  = new PQCommission();
			commission.setMinAmount(user.getMinAmount());
			commission.setMaxAmount(user.getMaxAmount());
			commission.setFixed(user.isFixed());
			commission.setType("POST");
			commission.setValue(user.getValue());
			commission.setService(service);
			commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
			if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
				commissionApi.save(commission);
			}

			PGDetails pgDetails = new PGDetails();
			pgDetails.setService(service);
			pgDetails.setSuccessURL(user.getSuccessURL());
			pgDetails.setReturnURL(user.getFailureURL());
			pgDetails.setIpAddress(user.getIpAddress());
			pgDetails.setPaymentGateway(user.isPaymentGateway());
			pgDetails.setStore(user.isStore());
			
			pgDetails.setToken(SecurityUtil.md5(user.getIpAddress()+"|"+user.getContactNo()));
			User merchant = userApi.findByUserName(user.getEmail());
			pgDetails.setUser(merchant);
			pgDetailsRepository.save(pgDetails);
			result.setMessage("Merchant Added Successfully");
			result.setStatus(ResponseStatus.SUCCESS);
			result.setDetails("Merchant Added and id is "+merchant.getId()+" and secret key is "+pgDetails.getToken());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<MerchantDTO> getAll(String type) {
		List<PGDetails> pgList = null;
		switch(type.toUpperCase()){
			case "STORE":
				pgList = pgDetailsRepository.findMerchantsOfStore(true);
				break;
			case "PG":
				pgList = pgDetailsRepository.findMerchantsOfPaymentGateway(true);
				break;
			default :
				pgList = (List<PGDetails>) pgDetailsRepository.findAll();
				break;
		}
		List<User> userList = ConvertUtil.convertFromPGDetails(pgList);
		List<MerchantDTO> merchantList = ConvertUtil.convertMerchantList(userList);
		return merchantList;
	}

}
