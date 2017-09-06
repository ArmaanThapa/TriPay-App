package com.tripayapp.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tripayapp.entity.*;
import com.tripayapp.model.*;
import com.thirdparty.model.request.AuthenticationDTO;
import com.thirdparty.model.request.PaymentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripayapp.model.PromoCodeDTO;
import com.tripayapp.model.PromoServiceCode;
import com.tripayapp.model.PromoTransactionDTO;
import com.tripayapp.model.SendMoneyBankDTO;
import com.tripayapp.model.ServiceType;
import com.tripayapp.model.Status;
import com.tripayapp.model.TelcoCircleDTO;
import com.tripayapp.model.TelcoOperatorDTO;
import com.tripayapp.model.TelcoPlansDTO;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.UserSessionDTO;
import com.tripayapp.model.mobile.ResponseStatus;

import javassist.compiler.SyntaxError;
import org.springframework.data.domain.*;

public class ConvertUtil {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private ConvertUtil() {

	}

	public static PayStoreDTO convertToPayStore(long id,String amount){
		PayStoreDTO dto = new PayStoreDTO();
		dto.setId(id);
		dto.setNetAmount(Double.parseDouble(amount));
		return dto;
	}
	public static TransactionListDTO convertListFromDetails(User u,PQTransaction t,PQCommission c){
		TransactionListDTO dto = new TransactionListDTO();
		dto.setId(t.getId());
		dto.setStatus(t.getStatus());
		dto.setAmount(t.getAmount());
		dto.setAuthority(u.getAuthority());
		dto.setCommission(c.getValue());
		dto.setContactNo(u.getUserDetail().getContactNo());
		dto.setEmail(u.getUserDetail().getEmail());
		dto.setCurrentBalance(t.getCurrentBalance());
		dto.setImage(u.getUserDetail().getImage());
		dto.setDebit(t.isDebit());
		dto.setDateOfTransaction(t.getCreated());
		dto.setDescription(t.getDescription());
		dto.setServiceType(t.getService().getName());
		dto.setTransactionRefNo(t.getTransactionRefNo());
		dto.setUsername(u.getUserDetail().getFirstName());
		return dto;
	}

	public static List<TransactionReport> getFromTransactionList(List<PQTransaction> transactions,PQServiceType serviceType){
		List<TransactionReport> reports = new ArrayList<>();
		for(PQTransaction p: transactions){
			if(p.getService().getServiceType().getName().equals(serviceType.getName())) {
				reports.add(convertListFromEntity(p));
			}
		}
		return reports;

	}

	public static TransactionReport convertListFromEntity(PQTransaction t){
		TransactionReport dto = new TransactionReport();
		dto.setStatus(t.getStatus());
		dto.setAmount(t.getAmount());
		dto.setDebit(t.isDebit());
		dto.setDescription(t.getDescription());
		dto.setTransactionRefNo(t.getTransactionRefNo());
		dto.setDate(dateTimeFormat.format(t.getCreated()));
		dto.setService(t.getService().getDescription());
		return dto;
	}


	public static final List<User> filteredList(List<User> userList,String reportType){
		List<User> filteredList = new ArrayList<>();
		System.err.println("reportType ::" + reportType);
			switch(reportType.toUpperCase()){
			
				case "ALL" :
						for(User user : userList) {
							String authority = user.getAuthority();
							if(authority.contains(Authorities.USER) || authority.contains(Authorities.MERCHANT)){
								filteredList.add(user);
							}
						}
						break;
				case "SETTLEMENT" :
						for (User user : userList) {
							String username = StartupUtil.SETTLEMENT;
							if(user.getUserDetail().equals(username)) {
								userList.add(user);
							}
						}
						break;
				case "COMMISSION" :
					for (User user : userList) {
						String username = StartupUtil.COMMISSION;
						if(user.getUsername().equals(username)){
							userList.add(user);
						}
					}
					break;
					
				default :
					break;
			}

		return filteredList;
	}

	public static final List<TransactionListDTO> convertFromLists(List<User> userList,List<PQTransaction> transactionList,List<PQCommission> commissionList){
		List<TransactionListDTO> resultList = new ArrayList<>();
		for(PQTransaction p: transactionList){
			long transactionAccount = p.getAccount().getAccountNumber();
			PQService service = p.getService();
			for(User u : userList) {
				long userAccount = u.getAccountDetail().getAccountNumber();
				if(userAccount == transactionAccount) {
						if(service != null) {
							PQCommission commission = getCommissionFromList(commissionList,service);
							if(commission != null){
								resultList.add(convertListFromDetails(u,p,commission));
							}
						}
				}
			}
		}
		return resultList;
	}

	public static PQCommission getCommissionFromList(List<PQCommission> commissionList,PQService service){
		PQCommission commission = null;
		long serviceId = service.getId();
		for(PQCommission c:commissionList){
			PQService serviceOfCommission = c.getService();
			if(serviceOfCommission != null) {
				long serviceIdInList = c.getService().getId();
				if(serviceIdInList == serviceId) {
					commission = c;
				}
			}
		}
		return commission;
	}

	public static List<BankTransferDTO> convertToBankTransfer(List<BankTransfer> bankTransfers){
  		List<BankTransferDTO> bankTransferList = new ArrayList<>();
		for(BankTransfer bt: bankTransfers){
			bankTransferList.add(convertToDTO(bt));
		}
		return bankTransferList;
	}

	public static List<User> convertFromPGDetails(List<PGDetails> pgDetails){
		List<User> userList = new ArrayList<>();
		if(pgDetails != null && !pgDetails.isEmpty()) {
			for (PGDetails p : pgDetails) {
				if (p != null) {
					userList.add(p.getUser());
				}
			}
		}
		return userList;
	}

	public static Page<MTransactionResponseDTO> convertFromList(List<MTransactionResponseDTO> transactionList,PagingDTO dto){
		Sort sort = new Sort(Sort.Direction.DESC,"id");
		Pageable page = new PageRequest(dto.getPage(),dto.getSize(),sort);
		Collections.sort(transactionList,new ComparatorUtil());
		int start = page.getOffset();
		int end = (start + page.getPageSize()) > transactionList.size() ? transactionList.size() : (start + page.getPageSize());
		List<MTransactionResponseDTO> subList = transactionList.subList(start,end);
		Page<MTransactionResponseDTO> resultSet = new PageImpl<MTransactionResponseDTO>(subList,page,transactionList.size());
		return resultSet;
	}


	public static List<MTransactionResponseDTO> getMerchantTransactions(List<PQTransaction> transactionList,List<User> userList){
		List<MTransactionResponseDTO> mTransactionLists = new ArrayList<>();
		for(PQTransaction transaction : transactionList){
			for(User user : userList) {
				if(transaction.getAccount().getAccountNumber() == user.getAccountDetail().getAccountNumber()) {
					mTransactionLists.add(ConvertUtil.convertFromUserAndTransaction(user,transaction));
				}
			}
		}
		return mTransactionLists;
	}

	public static MTransactionResponseDTO convertFromUserAndTransaction(User u,PQTransaction p){
		MTransactionResponseDTO dto = new MTransactionResponseDTO();
		if(u != null && p != null) {
			dto.setAmount(p.getAmount());
			dto.setContactNo(u.getUsername());
			dto.setEmail(u.getUserDetail().getEmail());
			dto.setCurrentBalance(p.getCurrentBalance());
			dto.setDate(p.getCreated());
			dto.setDebit(p.isDebit());
			dto.setStatus(p.getStatus());
			dto.setTransactionRefNo(p.getTransactionRefNo());
			dto.setDescription(p.getDescription());
			dto.setId(p.getId());
			return dto;
		}
		return dto;
	}

	public static BankTransferDTO convertToDTO(BankTransfer bankTransfer){

		BankTransferDTO dto = new BankTransferDTO();

		dto.setName(bankTransfer.getSender().getUserDetail().getFirstName());

		dto.setEmail(bankTransfer.getSender().getUserDetail().getEmail());

		dto.setMobileNumber(bankTransfer.getSender().getUsername());

		dto.setAmount(""+bankTransfer.getAmount());

		dto.setBankName(bankTransfer.getBankDetails().getBank().getName());

		dto.setIfscCode(bankTransfer.getBankDetails().getIfscCode());

		dto.setBeneficiaryAccountName(bankTransfer.getBeneficiaryName());

		dto.setBeneficiaryAccountNumber(""+bankTransfer.getBeneficiaryAccountNumber());

		dto.setVirtualAccount(""+bankTransfer.getSender().getAccountDetail().getAccountNumber());

		dto.setTransactionID(bankTransfer.getTransactionRefNo());

		return dto;
	}
	public static List<ServiceTypeDTO> convertServiceType(List<PQServiceType> servicesList){
		List<ServiceTypeDTO> serviceTypeList = new ArrayList<>();
		for(PQServiceType s:servicesList){
			serviceTypeList.add(convertPQServiceTypeToServiceTypeDTO(s));
		}
		return serviceTypeList;
	}

	public static AuthenticationDTO getByPayment(PaymentDTO dto){
		AuthenticationDTO authenticationDTO = new AuthenticationDTO();
		authenticationDTO.setTransactionID(dto.getTransactionID());
		authenticationDTO.setAmount(String.valueOf(dto.getNetAmount()));
		return authenticationDTO;

	}
	public static List<ServicesDTO> convertService(List<PQService> services){
		List<ServicesDTO> servicesList = new ArrayList<>();
		for(PQService s : services){
			servicesList.add(convertPQServiceToDTO(s));
		}
		return servicesList;
	}

	public static ServicesDTO convertPQServiceToDTO(PQService service){
		ServicesDTO servicesDTO = new ServicesDTO();
		servicesDTO.setDescription(service.getDescription());
		servicesDTO.setCode(service.getCode());
		servicesDTO.setOperatorCode(service.getOperatorCode());
		return servicesDTO;
	}

	public static ServiceTypeDTO convertPQServiceTypeToServiceTypeDTO(PQServiceType serviceType){
		ServiceTypeDTO serviceTypeDTO = new ServiceTypeDTO();
		serviceTypeDTO.setId(serviceType.getId());
		serviceTypeDTO.setName(serviceType.getName());
		serviceTypeDTO.setDescription(serviceType.getDescription());
		return serviceTypeDTO;
	}
	public static List<UserSessionDTO> convertSessionList(List<UserSession> userSession) {
		List<UserSessionDTO> dtoList = new ArrayList<>();
		for (UserSession session : userSession) {
			dtoList.add(convertSession(session));
		}
		return dtoList;
	}

	public static List<FavouriteDTO> convertTransactionList(List<PQTransaction> transaction) {
		List<FavouriteDTO> dtoList = new ArrayList<>();
		for (PQTransaction t : transaction) {
			dtoList.add(getByTransaction(t));
		}
		return dtoList;
	}
	public static FavouriteDTO getByTransaction(PQTransaction transaction){
		FavouriteDTO dto = new FavouriteDTO();
		try {
			if (transaction != null) {
				dto.setId(transaction.getId());
				dto.setAmount(transaction.getAmount());
				dto.setDate(transaction.getCreated());
				dto.setDebit(transaction.isDebit());
				dto.setFavourite(transaction.isFavourite());
				dto.setDescription(transaction.getDescription());
				dto.setStatus(transaction.getStatus());
				dto.setService(transaction.getService().getServiceType().getName());
				dto.setServiceCode(transaction.getService().getCode());
				dto.setTransactionRefNo(transaction.getTransactionRefNo());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
			return dto;
	}

	public static OnePayResponse get(PQTransaction transaction){
		OnePayResponse dto = new OnePayResponse();
		try {
			dto.setStatus(ResponseStatus.SUCCESS);
			dto.setServiceCode(transaction.getService().getCode());
			dto.setServiceName(transaction.getService().getName());
			dto.settStatus(transaction.getStatus());
			dto.setJson(transaction.getRequest());
		}catch(Exception ex){
			ex.printStackTrace();
		}
			return dto;
	}
	public static String convertAuthenticationDTO(AuthenticationDTO dto) {
		return dto.getToken() + "|" + dto.getAmount() + "|" + dto.getId() + "|" + dto.getTransactionID();
	}

	public static VNetDTO convertVNet(VNetDTO dto, String transactionRefNo) {
		VNetDTO newDTO = new VNetDTO();
		Double preciseAmount = Double.parseDouble(dto.getAmount());
		newDTO.setAmount(String.format("%.2f", preciseAmount));
		newDTO.setCrnNo("INR");
		newDTO.setMid("vPayQwik");
		newDTO.setMerchantName("vPayQwik");
		newDTO.setPid("10420249");
		newDTO.setItc(transactionRefNo);
		newDTO.setReturnURL(dto.getReturnURL());
		newDTO.setPrnNo(transactionRefNo);
		return newDTO;
	}

	public static UserSessionDTO convertSession(UserSession session) {
		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
		UserSessionDTO dto = new UserSessionDTO();
		dto.setUsername(session.getUser().getUsername());
		dto.setId(session.getId());
		dto.setSessionId(session.getSessionId());
		dto.setUserId(session.getUser().getId());
		dto.setUserType(session.getUser().getUserType());
		dto.setLastRequest(time.format(session.getLastRequest()));
		return dto;
	}

	public static List<TelcoOperatorDTO> convertTelcoOperatorList(List<TelcoOperator> telcoOperators) {
		List<TelcoOperatorDTO> dtoList = new ArrayList<TelcoOperatorDTO>();
		for (TelcoOperator telcoOperator : telcoOperators) {
			dtoList.add(convertTelcoOperator(telcoOperator));
		}
		return dtoList;
	}

	public static TelcoOperatorDTO convertTelcoOperator(TelcoOperator telcoOperator) {
		TelcoOperatorDTO dto = new TelcoOperatorDTO();
		dto.setCode(telcoOperator.getCode());
		dto.setServiceCode(telcoOperator.getServiceCode());
		dto.setName(telcoOperator.getName());
		return dto;
	}

	public static List<TelcoCircleDTO> convertTelcoCircleList(List<TelcoCircle> telcoCircles) {
		List<TelcoCircleDTO> dtoList = new ArrayList<>();
		for (TelcoCircle telcoCircle : telcoCircles) {
			dtoList.add(convertTelcoCircle(telcoCircle));
		}
		return dtoList;

	}

	public static TelcoCircleDTO convertTelcoCircle(TelcoCircle telcoCircle) {
		TelcoCircleDTO dto = new TelcoCircleDTO();
		dto.setCode(telcoCircle.getCode());
		dto.setName(telcoCircle.getName());
		return dto;
	}

	public static List<TelcoPlansDTO> convertTelcoPlansList(List<TelcoPlans> telcoPlans) {
		List<TelcoPlansDTO> dtoList = new ArrayList<>();
		for (TelcoPlans telcoPlan : telcoPlans) {
			dtoList.add(convertTelcoPlans(telcoPlan));
		}
		return dtoList;
	}

	public static TelcoPlansDTO convertTelcoPlans(TelcoPlans telcoPlans) {
		TelcoPlansDTO dto = new TelcoPlansDTO();
		dto.setAmount(telcoPlans.getAmount());
		dto.setDescription(telcoPlans.getDescription());
		dto.setOperatorCode(telcoPlans.getOperatorCode());
		dto.setPlanName(telcoPlans.getPlanName());
		dto.setPlanType(telcoPlans.getPlanType());
		dto.setSmsDaakCode(telcoPlans.getSmsDaakCode());
		dto.setState(telcoPlans.getState());
		dto.setValidity(telcoPlans.getValidity());
		return dto;
	}

	public static List<UserDTO> convertUserList(List<User> user) {
		List<UserDTO> dtoList = new ArrayList<UserDTO>();
		for (User u : user) {
			dtoList.add(convertUser(u));
		}
		return dtoList;
	}


	public static List<MerchantDTO> convertMerchantList(List<User> merchant) {
		List<MerchantDTO> dtoList = new ArrayList<>();
		for (User u : merchant) {
			dtoList.add(convertMerchant(u));
		}
		return dtoList;
	}

	public static MerchantDTO convertMerchant(User merchant){
		MerchantDTO merchantDTO = new MerchantDTO();
		try {
			merchantDTO.setId(merchant.getId());
			UserDetail merchantDetail = merchant.getUserDetail();
			merchantDTO.setContactNo((merchantDetail.getContactNo() == null) ? "" : merchantDetail.getContactNo());
			merchantDTO.setEmail((merchantDetail.getEmail() == null) ? "" : merchantDetail.getEmail());
			merchantDTO.setImage((merchantDetail.getImage() == null) ? "" : merchantDetail.getImage());
			merchantDTO.setName((merchantDetail.getFirstName() == null) ? "" : merchantDetail.getFirstName());
			merchantDTO.setDateOfRegistration(dateTimeFormat.format(merchant.getCreated()));
		}catch(NullPointerException ex){
			ex.printStackTrace();
		}
			return merchantDTO;
	}

	public static UserDTO convertUser(User u) {
		UserDTO dto = new UserDTO();
		dto.setUserId(String.valueOf(u.getId()));
		dto.setAddress((u.getUserDetail().getAddress() == null) ? "" : u.getUserDetail().getAddress());
		dto.setContactNo(u.getUserDetail().getContactNo());
		dto.setFirstName(u.getUserDetail().getFirstName());
		dto.setMiddleName((u.getUserDetail().getMiddleName() == null) ? "" : u.getUserDetail().getMiddleName());
		dto.setLastName(u.getUserDetail().getLastName());
		dto.setEmail(u.getUserDetail().getEmail());
		dto.setMobileStatus(u.getMobileStatus());
		dto.setEmailStatus(u.getEmailStatus());
		dto.setUserType(u.getUserType());
		dto.setUsername(u.getUsername());
		dto.setAuthority(u.getAuthority());
		dto.setDateOfBirth("" + u.getUserDetail().getDateOfBirth());
		dto.setGender(u.getUserDetail().getGender());
		dto.setMpin((u.getUserDetail().getMpin() == null) ? "" : u.getUserDetail().getMpin());
		dto.setImage((u.getUserDetail().getImage() == null) ? "" : u.getUserDetail().getImage());
		dto.setMpinPresent((u.getUserDetail().getMpin() == null) ? false : true);
		dto.setGcmId((u.getGcmId() == null) ? "" : u.getGcmId());
		LocationDetails location = u.getUserDetail().getLocation();
		if(location != null) {
			dto.setPinCode(location.getPinCode());
			dto.setCircleName(location.getCircleName());
			dto.setLocality(location.getLocality());
			dto.setDistrictName(location.getDistrictName());
		}
		return dto;
	}

	public static PromoCode convertPromotionCodeDTO(PromoCodeDTO promoDTO) {
		PromoCode promoCode = new PromoCode();
		try {
			promoCode.setPromoCode(promoDTO.getPromoCode());
			promoCode.setTerms(promoDTO.getTerms());
			promoCode.setStartDate(sdf.parse(promoDTO.getStartDate()));
			promoCode.setEndDate(sdf.parse(promoDTO.getEndDate()));
			promoCode.setValue(promoDTO.getValue());
			System.err.println(promoDTO.isFixed());
			promoCode.setFixed(promoDTO.isFixed());
			promoCode.setStatus(Status.Active);
			promoCode.setDescription(promoDTO.getDescription());
		}catch(Exception ex){
			ex.printStackTrace();
		}
			return promoCode;
	}

	public static String convertServiceType(ServiceType serviceType) {
		if (serviceType.getValue().equals("TopUp"))
			return "PTPS";
		else if (serviceType.getValue().equals("BillPay"))
			return "PBPS";
		else if (serviceType.getValue().equals("SendMoney"))
			return "PSMS";
		else if (serviceType.getValue().equals("LoadMoney"))
			return "PLMS";
		else
		return "";

	}
	public static List<String> getServiceCodeFromPromoServices(List<PromoServices> serviceList){
		List<String> serviceCodes = new ArrayList<>();
		for(PromoServices services: serviceList){
			serviceCodes.add(services.getService().getCode());
		}
		return serviceCodes;
	}
	public static PromoCodeDTO convertPromotionCode(PromoCode promoCode) {
		PromoCodeDTO promoDTO = new PromoCodeDTO();
		try {
			promoDTO.setPromoCodeId(promoCode.getId());
			promoDTO.setPromoCode(promoCode.getPromoCode());
			promoDTO.setTerms(promoCode.getTerms());
			promoDTO.setStartDate(""+promoCode.getStartDate());
			promoDTO.setEndDate(""+promoCode.getEndDate());
			promoDTO.setFixed(promoCode.isFixed());
			promoDTO.setValue(promoCode.getValue());
			promoDTO.setStatus(promoCode.getStatus());
			promoDTO.setDescription(promoCode.getDescription());

		}catch(Exception e){
			e.printStackTrace();
		}

		return promoDTO;

	}

	public static List<PromoCodeDTO> convertPromotionCodeList(List<PromoCode> promoCodes) {
		List<PromoCodeDTO> promotionCodeDTOs = new ArrayList<PromoCodeDTO>();
		for (PromoCode promoCode : promoCodes) {
			promotionCodeDTOs.add(convertPromotionCode(promoCode));
		}
		return promotionCodeDTOs;
	}

	public static BankTransfer bankTransfer(SendMoneyBankDTO dto,String transactionRefNo,BankDetails details,User u) {
		BankTransfer transfer = new BankTransfer();
		transfer.setAmount(Double.parseDouble(dto.getAmount()));
		transfer.setTransactionRefNo(transactionRefNo+"D");
		transfer.setBankDetails(details);
		transfer.setBeneficiaryAccountNumber(dto.getAccountNumber());
		transfer.setSender(u);
		transfer.setBeneficiaryName(dto.getAccountName());
		return transfer;

	}
	
	public static MBankTransfer mBankTransfer(SendMoneyBankDTO dto,String transactionRefNo,BankDetails details,User u) {
		MBankTransfer transfer = new MBankTransfer();
		transfer.setAmount(Double.parseDouble(dto.getAmount()));
		transfer.setTransactionRefNo(transactionRefNo+"D");
		transfer.setBankDetails(details);
		transfer.setBeneficiaryAccountNumber(dto.getAccountNumber());
		transfer.setSender(u);
		transfer.setBeneficiaryName(dto.getAccountName());
		return transfer;

	}

	public static PromoTransactionDTO convertPromoTransaction(List<PQTransaction> trasList, PromoCode code,List<PromoServices> servicesList) {
		PromoTransactionDTO dto = null;
		double amount = Double.parseDouble(code.getTerms());
		if(trasList != null && trasList.size() > 0) {
                System.err.println("transList in not null "+trasList);
				for(PromoServices services : servicesList) {
					PQService service = services.getService();
					System.err.println("Service Code "+service.getCode());
					for (PQTransaction pqTransaction : trasList) {
						System.err.println("PQTransaction List ::"+pqTransaction);
					try {
						System.err.println(service.getCode().equalsIgnoreCase(pqTransaction.getService().getCode()));
						if (service.getCode().equalsIgnoreCase(pqTransaction.getService().getCode())) {
							if (amount <= pqTransaction.getAmount() && pqTransaction.getStatus().equals(Status.Success)) {
								System.err.println("PromoCodeByService Response  :: " + service);
								System.err.println("Transaction Service Code :: " + pqTransaction.getService().getOperatorCode());
								dto = new PromoTransactionDTO();
								dto.setAmount(String.valueOf(pqTransaction.getAmount()));
								dto.setServiceCode(service.getCode());
								dto.setTransactionDate(sdf.format(pqTransaction.getCreated()));
							} else {
								System.err.println("Inside check amount false");
							}
						}
						}catch(NullPointerException e){
							e.printStackTrace();
						}
				}
			}
			return dto;
		}else{
			return null;
		}
	}
}
